package auth;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.auth.ActualizarPasswordDto;
import co.edu.uniquindio.dto.common.auth.SolicitudEmailDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de pruebas RestablecerPasswordControllerTest

  Esta clase valida el flujo completo de restablecimiento de contraseña
  dentro del sistema Store-IT:
   1. Solicitud de restablecimiento (envío de email con código)
   2. Verificación del código recibido
   3. Actualización de la nueva contraseña

  Se prueban casos exitosos y escenarios de error
  como cuentas inactivas, eliminadas, inexistentes,
  códigos inválidos o contraseñas débiles.
 */
@SpringBootTest(classes = Main.class)       // Levanta el contexto de Spring Boot con toda la app
@AutoConfigureMockMvc(addFilters = false)  // Deshabilita filtros de seguridad en pruebas
@Transactional                             // Revierten los cambios en la BD al final de cada test
public class RestablecerPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc; // Cliente HTTP simulado para pruebas

    @Autowired
    private ObjectMapper objectMapper; // Serializa/Deserializa objetos a JSON

    @Autowired
    private ClienteRepo clienteRepo; // Acceso a BD de clientes en entorno de pruebas

    @Autowired
    private PasswordEncoder passwordEncoder; // Para validar codificación de contraseñas

    // Objetos de prueba reutilizables
    private Cliente clienteBase;
    private SolicitudEmailDto solicitudEmailDto;
    private VerificacionCodigoDto verificacionCodigoDto;
    private ActualizarPasswordDto actualizarPasswordDto;

    @BeforeEach
    void setUp() {
        // Configurar DTOs base para las pruebas
        verificacionCodigoDto = new VerificacionCodigoDto("cliente@test.com", "ABC123");
        solicitudEmailDto = new SolicitudEmailDto("cliente@test.com");
        actualizarPasswordDto = new ActualizarPasswordDto("cliente@test.com", "NuevaPassword123");

        // Crear cliente base
        clienteBase = new Cliente();
        clienteBase.setNombre("Juan");
        clienteBase.setTelefono("9999999999");
        clienteBase.setTipoCliente(TipoCliente.NATURAL);

        // Usuario asociado al cliente
        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword(passwordEncoder.encode("Password123")); // contraseña encriptada
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        clienteBase.setUser(user);

        // Código temporal para restablecer contraseña
        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.RESTABLECER_PASSWORD);
        codigo.setClave("ABC123");
        codigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        clienteBase.getUser().setCodigo(codigo);

        // Guardar cliente en la BD de prueba
        clienteRepo.save(clienteBase);
    }

    // --------- TESTS PARA SOLICITAR RESTABLECIMIENTO ---------

    @Test
    void solicitarRestablecimientoPassword_Exitoso_DeberiaRetonar200() throws Exception {
        // Caso: usuario válido, debería enviar el email con el código
        mockMvc.perform(post("/api/auth/restablecer-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudEmailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje").value("Solicitud de restablecimiento de contraseña enviada"));
    }

    @Test
    void solicitarRestablecimientoPassword_Fallido_CuentaInactiva_DeberiaRetonar418() throws Exception {
        // Caso: cuenta existe pero está inactiva
        clienteBase.getUser().setEstadoCuenta(EstadoCuenta.INACTIVA);
        clienteRepo.save(clienteBase);

        mockMvc.perform(post("/api/auth/restablecer-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudEmailDto)))
                .andExpect(status().isIAmATeapot()) // código 418 usado para manejar este caso especial
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("La cuenta ya existe pero está inactiva. Hemos enviado un nuevo código de verificación a tu correo."));
    }

    @Test
    void solicitarRestablecimientoPassword_Fallido_CuentaEliminada_DeberiaRetonar418() throws Exception {
        // Caso: cuenta está eliminada
        clienteBase.getUser().setEstadoCuenta(EstadoCuenta.ELIMINADO);
        clienteRepo.save(clienteBase);

        mockMvc.perform(post("/api/auth/restablecer-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudEmailDto)))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("La cuenta se encuentra eliminada."));
    }

    @Test
    void solicitarRestablecimientoPassword_Fallido_CuentaNoEncontrada_DeberiaRetonar404() throws Exception {
        // Caso: el email no corresponde a ningún usuario
        SolicitudEmailDto dto = new SolicitudEmailDto("correo_incorrecto@gmail.com");

        mockMvc.perform(post("/api/auth/restablecer-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("Persona no encontrada o no registrada"));
    }

    // --------- TESTS PARA VERIFICAR EL CÓDIGO ---------

    @Test
    void verificarCodigoPassword_CodigoCorrecto_DeberiaRetornar200() throws Exception {
        // Caso exitoso: código válido
        mockMvc.perform(post("/api/auth/verificar-codigo-restablecimiento-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje").value("Código verificado exitosamente"));
    }

    @Test
    void verificarCodigoPassword_CodigoIncorrecto_DeberiaRetornar401() throws Exception {
        // Caso: código ingresado no coincide
        verificacionCodigoDto = new VerificacionCodigoDto("cliente@test.com", "123ABC");

        mockMvc.perform(post("/api/auth/verificar-codigo-restablecimiento-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("El código proporcionado no coincide"));
    }

    @Test
    void verificarCodigoPassword_EmailNoRegistrado_DeberiaRetornar404() throws Exception {
        // Caso: se intenta verificar un email inexistente
        verificacionCodigoDto = new VerificacionCodigoDto("correo_incorrecto@test.com", "123ABC");

        mockMvc.perform(post("/api/auth/verificar-codigo-restablecimiento-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("Persona no encontrada o no registrada"));
    }

    // --------- TESTS PARA ACTUALIZAR LA CONTRASEÑA ---------

    @Test
    void actualizarPassword_Exitoso_DeberiaRetornar200() throws Exception {
        // Caso exitoso: nueva contraseña válida
        mockMvc.perform(put("/api/auth/actualizar-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje").value("Contraseña actualizada correctamente"));

        // Validación adicional: confirmar que la BD guardó la contraseña actualizada encriptada
        Cliente updated = clienteRepo.findByUser_Email(clienteBase.getUser().getEmail()).orElseThrow();
        assertThat(passwordEncoder.matches(actualizarPasswordDto.nuevaPassword(), updated.getUser().getPassword())).isTrue();
    }

    @Test
    void actualizarPassword_EmailNoRegistrado_DeberiaRetornar404() throws Exception {
        // Caso: se intenta cambiar contraseña de un usuario inexistente
        actualizarPasswordDto = new ActualizarPasswordDto("email_Incorrecto@gmail.com","Password123");

        mockMvc.perform(put("/api/auth/actualizar-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPasswordDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("Persona no encontrada o no registrada"));
    }

    @Test
    void actualizarPassword_PasswordNoValida_DeberiaRetornar400() throws Exception {
        // Caso: la nueva contraseña no cumple requisitos mínimos
        actualizarPasswordDto = new ActualizarPasswordDto("cliente@test.com","Pas");

        mockMvc.perform(put("/api/auth/actualizar-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPasswordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

}
