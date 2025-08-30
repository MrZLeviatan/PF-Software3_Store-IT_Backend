package users.cliente;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.controller.cliente.ClienteBannerController;
import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.model.enums.TipoCliente;
import co.edu.uniquindio.service.users.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteBannerController.class)
@ContextConfiguration(classes = Main.class) // 👈 le dices qué configuración usar
@AutoConfigureMockMvc(addFilters = false) // 🚀 ignora seguridad
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos en JSON

    @MockitoBean
    private ClienteService clienteServicio; // Mockeamos la capa de servicio

    private CrearClienteDto crearClienteDto;

    @BeforeEach
    void setUp() {
        // Creamos un DTO válido para las pruebas
        CrearUserDto userDto = new CrearUserDto("nikis281002@gmail.com", "Password123");
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Medellín", 6.25184, -75.56359);

        crearClienteDto = new CrearClienteDto(
                "Juan Pérez",
                "3001234567",
                "+57",
                null,
                null,
                userDto,
                ubicacionDto,
                TipoCliente.NATURAL,
                null
        );
    }

    @Test
    void registrarCliente_DeberiaRetornar200() throws Exception {
        // 1. No necesitamos configurar el mock porque el servicio no retorna nada
        Mockito.doNothing().when(clienteServicio).registrarCliente(Mockito.any());

        // 2. Ejecutamos la petición con MockMvc
        mockMvc.perform(post("/api/clienteBanner/registro") // tu ruta real aquí
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteDto)))
                .andExpect(status().isOk()) // Debe responder 200 OK
                .andExpect(jsonPath("$.mensaje").value("Registro logrado exitosamente."))
                .andExpect(jsonPath("$.error").value(false));
    }



    @Test
    void registrarCliente_DeberiaRetornar400_CuandoFaltanDatos() throws Exception {
        // 1. Creamos un DTO inválido (sin email)
        CrearUserDto userInvalido = new CrearUserDto("", "123");
        CrearClienteDto clienteInvalido = new CrearClienteDto(
                "", // nombre vacío
                "", // telefono vacío
                "", // codigoPais vacío
                null,
                null,
                userInvalido,
                new UbicacionDto("", "", null, null), // ubicación inválida
                TipoCliente.NATURAL,
                null
        );

        // 2. Ejecutamos la petición
        mockMvc.perform(post("/api/clienteBanner/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest()); // Debería fallar validación
    }
}
