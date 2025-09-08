package users.cliente;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de pruebas para la verificación del registro de clientes en Store-IT.

  Aquí se validan los distintos escenarios al momento de activar la cuenta de un cliente:
  - Caso exitoso (cliente se activa correctamente).
  - Email no registrado en el sistema.
  - Código de verificación incorrecto.
  - Código expirado.
  - Cuenta ya se encuentra activa.
  - Email con formato inválido.

 * Se usan pruebas de integración con Spring Boot, MockMvc y una base de datos en memoria,
 * permitiendo verificar el comportamiento del endpoint correspondiente.
 */
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class VerificarRegistroClienteTest {

    @Autowired
    private MockMvc mockMvc; // Simula las llamadas HTTP a los endpoints del backend

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos Java a JSON y viceversa

    @Autowired
    private ClienteRepo clienteRepo; // Repositorio de clientes para manipular datos de prueba

    private Cliente clienteBase; // Cliente base reutilizado en cada prueba

    @BeforeEach
    void setUp() {
        // Se prepara un cliente "inactivo" con un código válido antes de cada prueba
        clienteBase = new Cliente();
        clienteBase.setNombre("Juan");
        clienteBase.setTelefono("9999999999");
        clienteBase.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.INACTIVA);
        clienteBase.setUser(user);

        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigo.setClave("55B83C");
        codigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        clienteBase.getUser().setCodigo(codigo);

        clienteRepo.save(clienteBase);
    }

    // Caso exitoso: cliente verifica su cuenta correctamente
    @Test
    void verificarCliente_DeberiaRetornar200YActivarCuenta() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto("cliente@test.com", "55B83C");

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cliente verificado con éxito."))
                .andExpect(jsonPath("$.error").value(false));
    }

    // Email no existe en el sistema
    @Test
    void verificarCliente_EmailNoExiste_DeberiaRetornar404() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto("no_existe@test.com", "XYZ789");

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(true));
    }

    // Código incorrecto
    @Test
    void verificarCliente_CodigoInvalido_DeberiaRetornar401() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto("cliente@test.com", "CODIGO_ERRONEO");

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(true));
    }

    // Código expirado
    @Test
    void verificarCliente_CodigoExpirado_DeberiaRetornar401() throws Exception {
        // Se fuerza la expiración del código antes de hacer la petición
        clienteBase.getUser().getCodigo().setFechaExpiracion(LocalDateTime.now().minusMinutes(20));
        clienteRepo.save(clienteBase);

        VerificacionCodigoDto dto = new VerificacionCodigoDto("cliente@test.com", "55B83C");

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(true));
    }

    // Cuenta ya estaba activa previamente
    @Test
    void verificarCliente_CuentaYaActiva_DeberiaRetornar422() throws Exception {
        clienteBase.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
        clienteRepo.save(clienteBase);

        VerificacionCodigoDto dto = new VerificacionCodigoDto("cliente@test.com", "55B83C");

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value(true));
    }

    // Email con formato inválido
    @Test
    void verificarCliente_EmailInvalido_DeberiaRetornar400() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto("email_invalido", "ABC123");

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }
}
