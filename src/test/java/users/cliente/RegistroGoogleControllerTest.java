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

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc(addFilters = false) // Ignorar seguridad para pruebas
@Transactional
public class RegistroGoogleControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos a JSON

    @Autowired
    private ClienteRepo clienteRepo;

    private CrearClienteGoogleDto crearClienteGoogleDto;

    private UbicacionDto ubicacionDto;

    @BeforeEach
    void setUp() {

       ubicacionDto = new UbicacionDto("Colombia", "Medellín", 6.25184, -75.56359);

        crearClienteGoogleDto = new CrearClienteGoogleDto(
                "Juan Pérez",
                "3001234990",
                "CO",
                null,
                null,
                "googleuser@test.com",
                ubicacionDto
        );
    }


    @Test
    void registrarClienteGoogle_Exito_DeberiaRetornar200() throws Exception {


        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro logrado exitosamente."))
                .andExpect(jsonPath("$.error").value(false));


        // Verificar que se haya guardado en la BD
        Optional<Cliente> clienteGuardado = clienteRepo.findByUser_Email("googleuser@test.com");
        assert(clienteGuardado.isPresent());
    }


    @Test
    void registrarClienteGoogle_EmailDuplicadoCuentaActiva_DeberiaRetornar400() throws Exception {
        // Creamos cliente con estado Activado manualmente en repo
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


        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo electrónico ya se encuentra registrado."))
                .andExpect(jsonPath("$.error").value(true));
    }


    @Test
    void registrarCliente_EmailDuplicadoCuentaEliminada_DeberiaRetornar400() throws Exception {
        // Creamos cliente con estado ELIMINADO manualmente en repo
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


        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.mensaje").value("El correo pertenece a una cuenta eliminada."))
                .andExpect(jsonPath("$.error").value(true));
    }


    @Test
    void registrarCliente_TelefonoDuplicado_DeberiaRetornar400() throws Exception {

        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)));

        CrearClienteGoogleDto crearClienteGoogleDto2 = new CrearClienteGoogleDto(
                "Juan Pérez",
                "3001234990",
                "CO",
                null,
                null,
                "googleuser23@test.com",
                ubicacionDto
        );

        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El teléfono ya está registrado"));
    }


}
