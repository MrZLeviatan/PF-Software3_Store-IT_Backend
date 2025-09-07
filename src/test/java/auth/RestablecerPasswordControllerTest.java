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

@SpringBootTest(classes = Main.class)       // Levantamos el test con todo y Spring
@AutoConfigureMockMvc(addFilters = false)  // Ignora seguridad para pruebas
@Transactional
public class RestablecerPasswordControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepo clienteRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Cliente clienteBase;

    private SolicitudEmailDto solicitudEmailDto;

    private VerificacionCodigoDto verificacionCodigoDto;

    private ActualizarPasswordDto actualizarPasswordDto;


    @BeforeEach
    void setUp() {

        verificacionCodigoDto = new VerificacionCodigoDto("cliente@test.com", "ABC123");

        solicitudEmailDto = new SolicitudEmailDto("cliente@test.com");

        actualizarPasswordDto = new ActualizarPasswordDto("cliente@test.com", "NuevaPassword123");


        // Crear un cliente reutilizable para todos los tests
        clienteBase = new Cliente();
        clienteBase.setNombre("Juan");
        clienteBase.setTelefono("9999999999");
        clienteBase.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword(passwordEncoder.encode("Password123"));
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        clienteBase.setUser(user);

        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.RESTABLECER_PASSWORD);
        codigo.setClave("ABC123");
        codigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        clienteBase.getUser().setCodigo(codigo);

        clienteRepo.save(clienteBase);
    }


    @Test
    void solicitarRestablecimientoPassword_Exitoso_DeberiaRetonar200() throws Exception {

        mockMvc.perform(post("/api/auth/restablecer-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudEmailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje").value("Solicitud de restablecimiento de contraseña enviada"));
    }


    @Test
    void solicitarRestablecimientoPassword_Fallido_CuentaInactiva_DeberiaRetonar418() throws Exception {

        clienteBase.getUser().setEstadoCuenta(EstadoCuenta.INACTIVA);
        clienteRepo.save(clienteBase);

        mockMvc.perform(post("/api/auth/restablecer-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudEmailDto)))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("La cuenta ya existe pero está inactiva. Hemos enviado un nuevo código de verificación a tu correo."));
    }


    @Test
    void solicitarRestablecimientoPassword_Fallido_CuentaEliminada_DeberiaRetonar418() throws Exception {


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


        SolicitudEmailDto dto = new SolicitudEmailDto("correo_incorrecto@gmail.com");

        mockMvc.perform(post("/api/auth/restablecer-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("Persona no encontrada o no registrada"));
    }


    @Test
    void verificarCodigoPassword_CodigoCorrecto_DeberiaRetornar200() throws Exception {

        mockMvc.perform(post("/api/auth/verificar-codigo-restablecimiento-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje").value("Código verificado exitosamente"));

    }


    @Test
    void verificarCodigoPassword_CodigoIncorrecto_DeberiaRetornar401() throws Exception {

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

        verificacionCodigoDto = new VerificacionCodigoDto("correo_incorrecto@test.com", "123ABC");


        mockMvc.perform(post("/api/auth/verificar-codigo-restablecimiento-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("Persona no encontrada o no registrada"));

    }


    @Test
    void actualizarPassword_Exitoso_DeberiaRetornar200() throws Exception {

        mockMvc.perform(put("/api/auth/actualizar-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPasswordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje").value("Contraseña actualizada correctamente"));

        // Verificación extra: comprobar que BD guardó nueva contraseña
        Cliente updated = clienteRepo.findByUser_Email(clienteBase.getUser().getEmail()).orElseThrow();
        assertThat(passwordEncoder.matches(actualizarPasswordDto.nuevaPassword(), updated.getUser().getPassword())).isTrue();

    }


    @Test
    void actualizarPassword_EmailNoRegistrado_DeberiaRetornar404() throws Exception {

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

        actualizarPasswordDto = new ActualizarPasswordDto("cliente@test.com","Pas");

        mockMvc.perform(put("/api/auth/actualizar-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizarPasswordDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));

    }







}
