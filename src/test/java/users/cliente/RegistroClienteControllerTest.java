package users.cliente;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Main.class)       // Levantamos el test con todo y Spring
@AutoConfigureMockMvc(addFilters = false)  // Ignora seguridad para pruebas
@Transactional
public class RegistroClienteControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos en JSON

    @Autowired
    private ClienteRepo clienteRepo;


    private CrearClienteDto crearClienteValido() {
        CrearUserDto userDto = new CrearUserDto("cliente@test.com", "Password123");
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Medellín", 6.25184, -75.56359);

        return new CrearClienteDto(
                "Juan Pérez",
                "3001234990",
                "CO",
                null,
                null,
                userDto,
                ubicacionDto,
                TipoCliente.NATURAL,
                null
        );
    }

    // ✅ Caso exitoso
    @Test
    void registrarCliente_DeberiaRetornar200YMensajeExito() throws Exception {
        CrearClienteDto dto = crearClienteValido();

        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro logrado exitosamente."))
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void registrarClienteGoogle_DeberiaRetornar200YMensajeExito() throws Exception {
        CrearClienteDto dto = crearClienteValido();
    }


    // ❌ Email duplicado -> cuenta activada
    @Test
    void registrarCliente_EmailDuplicadoCuentaActiva_DeberiaRetornar400() throws Exception {
        // Creamos cliente con estado Activado manualmente en repo
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setTelefono("9999999999");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        cliente.setUser(user);
        clienteRepo.save(cliente);

        CrearClienteDto dto = crearClienteValido();

        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo electrónico ya se encuentra registrado."))
                .andExpect(jsonPath("$.error").value(true));
    }


    // ❌ Email duplicado -> cuenta inactiva
    @Test
    void registrarCliente_EmailDuplicado_DeberiaRetornar400() throws Exception {
        CrearClienteDto dto = crearClienteValido();

        // Guardamos uno primero
        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Intentamos registrar el mismo email otra vez
        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La cuenta ya existe pero está inactiva. Hemos enviado un nuevo código de verificación a tu correo."));

        // 🔎 Verificamos que se generó un nuevo código en el cliente
        Cliente actualizado = clienteRepo.findByUser_Email("cliente@test.com").orElseThrow();
        assertNotNull(actualizado.getUser().getCodigo());
        assertEquals(TipoCodigo.VERIFICACION_2FA, actualizado.getUser().getCodigo().getTipoCodigo());
    }


    // ❌ Email duplicado -> cuenta eliminada
    @Test
    void registrarCliente_EmailDuplicadoCuentaEliminada_DeberiaRetornar400() throws Exception {
        // Creamos cliente con estado ELIMINADO manualmente en repo
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setTelefono("9999999999");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.ELIMINADO);
        cliente.setUser(user);
        clienteRepo.save(cliente);

        CrearClienteDto dto = crearClienteValido();

        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.mensaje").value("El correo pertenece a una cuenta eliminada.")) // ajusta a MensajeError.EMAIL_CUENTA_ELIMINADA
                .andExpect(jsonPath("$.error").value(true));
    }


    // ❌ Teléfono duplicado
    @Test
    void registrarCliente_TelefonoDuplicado_DeberiaRetornar400() throws Exception {
        CrearClienteDto dto1 = crearClienteValido();

        // Guardamos el primero
        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        // Segundo cliente con mismo teléfono pero email distinto
        CrearUserDto otroUser = new CrearUserDto("otro@test.com", "Password123");
        CrearClienteDto dto2 = new CrearClienteDto(
                "Pedro Gómez",
                "3001234990", // mismo teléfono
                "CO",
                null,
                null,
                otroUser,
                dto1.ubicacion(),
                TipoCliente.NATURAL,
                null
        );

        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El teléfono ya está registrado"));
    }


    // ❌ Cliente jurídico sin NIT
    @Test
    void registrarCliente_JuridicoSinNit_DeberiaRetornar400() throws Exception {
        CrearUserDto userDto = new CrearUserDto("empresa@test.com", "Password123");
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Bogotá", 4.711, -74.0721);

        CrearClienteDto dto = new CrearClienteDto(
                "Empresa XYZ",
                "3012345678",
                "CO",
                null,
                null,
                userDto,
                ubicacionDto,
                TipoCliente.JURIDICO,
                "" // NIT vacío -> debería lanzar ElementoNulosException
        );

        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Falta de datos para completar el registro"));
    }


    // ❌ Validación de @Valid (email inválido)
    @Test
    void registrarCliente_EmailInvalido_DeberiaRetornar400() throws Exception {
        CrearUserDto userDto = new CrearUserDto("correo_invalido", "Password123"); // email inválido
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Cali", 3.4516, -76.5320);

        CrearClienteDto dto = new CrearClienteDto(
                "Carlos López",
                "3023456789",
                "CO",
                null,
                null,
                userDto,
                ubicacionDto,
                TipoCliente.NATURAL,
                null
        );

        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists()) // El mensaje viene del Bean Validation
                .andExpect(jsonPath("$.error").value(true));
    }

}

