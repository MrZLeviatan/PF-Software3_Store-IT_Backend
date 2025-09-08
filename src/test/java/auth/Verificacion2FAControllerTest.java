package auth;

import co.edu.uniquindio.Main;
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
import org.springframework.test.web.servlet.MockMvc;

import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de pruebas Verificacion2FAControllerTest

  Simula el proceso de verificación en el login con doble factor de autenticación (2FA)
  dentro del sistema Store-IT.

  Se asegura que el endpoint de verificación de códigos:
   - Valide un inicio de sesión exitoso
   - Maneje correos inexistentes
   - Detecte códigos incorrectos
   - Rechace códigos expirados

 * Usa MockMvc para simular llamadas HTTP sin necesidad de desplegar el servidor completo.
 */
@SpringBootTest(classes = Main.class)       // Levanta el contexto de Spring Boot con toda la app
@AutoConfigureMockMvc(addFilters = false)  // Deshabilita filtros de seguridad en las pruebas
@Transactional                             // Garantiza que los cambios de BD se revierten tras cada test
public class Verificacion2FAControllerTest {

    @Autowired
    private MockMvc mockMvc; // Cliente simulado para ejecutar peticiones HTTP en los tests

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos a JSON y viceversa

    @Autowired
    private ClienteRepo clienteRepo; // Acceso a la BD simulada en pruebas

    private VerificacionCodigoDto verificacionCodigoDto; // DTO con email y código ingresados por el cliente
    private Cliente clienteBase; // Cliente de prueba que se crea en memoria y se guarda en BD

    @BeforeEach
    void setUp() {
        // Configura datos de prueba antes de cada test
        // Caso válido: correo existente con código correcto
        verificacionCodigoDto = new VerificacionCodigoDto(
                "cliente@test.com",   // email registrado en BD
                "55B83C"              // código correcto asignado
        );

        // Crear cliente base reutilizable
        clienteBase = new Cliente();
        clienteBase.setNombre("Juan");
        clienteBase.setTelefono("9999999999");
        clienteBase.setTipoCliente(TipoCliente.NATURAL);

        // Datos del usuario asociado al cliente
        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        clienteBase.setUser(user);

        // Código 2FA válido y vigente
        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigo.setClave("55B83C");
        codigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15)); // no expirado
        clienteBase.getUser().setCodigo(codigo);

        // Persistir cliente en la BD para que los tests lo encuentren
        clienteRepo.save(clienteBase);
    }

    @Test
    void verificarLogin_Exitoso_DeberiaRetornar200() throws Exception {
        // Caso feliz: email y código correctos
        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isOk()) // Espera 200
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje.token").exists()); // Se genera un token
    }

    @Test
    void verificarLogin_EmailNoExiste_DeberiaRetornar404() throws Exception {
        // Caso: correo no registrado
        VerificacionCodigoDto dto = new VerificacionCodigoDto("noexiste@test.com", "123456");

        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound()) // 404 por usuario inexistente
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void verificarLogin_CodigoIncorrecto_DeberiaRetornar401() throws Exception {
        // Caso: correo existe pero código es incorrecto
        clienteBase.getUser().getCodigo().setClave("345678");
        clienteRepo.save(clienteBase);

        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isUnauthorized()) // 401 por credenciales incorrectas
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("El código proporcionado no coincide"));
    }

    @Test
    void verificarLogin_CodigoExpirado_DeberiaRetornar410() throws Exception {
        // Caso: código ha expirado
        clienteBase.getUser().getCodigo().setFechaExpiracion(LocalDateTime.now().minusMinutes(15));
        clienteRepo.save(clienteBase);

        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isUnauthorized()) // aquí podrías usar 401 o 410 dependiendo de tu diseño
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("El código proporcionado a expirado"));
    }

}
