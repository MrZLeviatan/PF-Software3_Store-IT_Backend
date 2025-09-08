package users.cliente;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Clase de pruebas para el controlador de registro de clientes mediante Google en Store-IT.

  Se validan distintos escenarios relacionados con el registro de un nuevo cliente:
  - Registro exitoso de un cliente nuevo.
  - Intento de registro con un correo ya asociado a una cuenta activa.
  - Intento de registro con un correo asociado a una cuenta eliminada.
  - Intento de registro con un teléfono duplicado.

 * Estas pruebas son de integración y usan MockMvc para simular peticiones HTTP
 * al endpoint correspondiente. También se verifica el estado en la base de datos
 * mediante ClienteRepo.
 */
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc(addFilters = false) // Ignora seguridad en pruebas
@Transactional
public class RegistroGoogleControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite simular peticiones HTTP al API

    @Autowired
    private ObjectMapper objectMapper; // Serializa y deserializa objetos a JSON

    @Autowired
    private ClienteRepo clienteRepo; // Acceso al repositorio de clientes para validaciones

    private CrearClienteGoogleDto crearClienteGoogleDto; // DTO base para los tests
    private UbicacionDto ubicacionDto; // Información de ubicación asociada al cliente

    @BeforeEach
    void setUp() {
        // Inicializa un DTO de ubicación y un cliente base de prueba
        ubicacionDto = new UbicacionDto("Colombia", "Medellín", 6.25184, -75.56359);

        crearClienteGoogleDto = new CrearClienteGoogleDto(
                "Juan Pérez",
                "3001234990",
                "CO",
                null,
                null,
                "googleuser@test.com",
                "Password123",
                ubicacionDto
        );
    }

    // Registro exitoso de un cliente con datos válidos
    @Test
    void registrarClienteGoogle_Exito_DeberiaRetornar200() throws Exception {
        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro logrado exitosamente."))
                .andExpect(jsonPath("$.error").value(false));

        // Verificación en base de datos
        Optional<Cliente> clienteGuardado = clienteRepo.findByUser_Email("googleuser@test.com");
        assert(clienteGuardado.isPresent());
    }

    // Intento de registro con email duplicado y cuenta activa
    @Test
    void registrarClienteGoogle_EmailDuplicadoCuentaActiva_DeberiaRetornar400() throws Exception {
        // Crear cliente en BD con estado activo
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setTelefono("3001234990");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("googleuser@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        cliente.setUser(user);
        clienteRepo.save(cliente);

        // Intento de registrar otro cliente con el mismo email
        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo electrónico ya se encuentra registrado."))
                .andExpect(jsonPath("$.error").value(true));
    }

    // Intento de registro con email duplicado pero cuenta eliminada
    @Test
    void registrarCliente_EmailDuplicadoCuentaEliminada_DeberiaRetornar400() throws Exception {
        // Crear cliente en BD con estado eliminado
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setTelefono("3001234990");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("googleuser@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.ELIMINADO);
        cliente.setUser(user);
        clienteRepo.save(cliente);

        // Intento de registrar otro cliente con el mismo email
        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.mensaje").value("El correo pertenece a una cuenta eliminada."))
                .andExpect(jsonPath("$.error").value(true));
    }

    // Intento de registro con un teléfono ya registrado
    @Test
    void registrarCliente_TelefonoDuplicado_DeberiaRetornar400() throws Exception {
        // Registrar un cliente con el teléfono base
        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(crearClienteGoogleDto)));

        // Intentar registrar un cliente nuevo con mismo teléfono pero diferente correo
        CrearClienteGoogleDto crearClienteGoogleDto2 = new CrearClienteGoogleDto(
                "Juan Pérez",
                "3001234990",
                "CO",
                null,
                null,
                "googleuser23@test.com",
                "Password123",
                ubicacionDto
        );

        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El teléfono ya está registrado"));
    }
}
