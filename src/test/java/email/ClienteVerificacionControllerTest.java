package email;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.model.enums.TipoCliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc(addFilters = false)

public class ClienteVerificacionControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos en JSON

    private CrearClienteDto crearClienteDto;

    @BeforeEach
    void setUp() {
        CrearUserDto userDto = new CrearUserDto("nikis281002@gmail.com", "Password123");
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Medellín", 6.25184, -75.56359);

        crearClienteDto = new CrearClienteDto(
                "Juan Pérez",
                "3001234567",
                "CO",
                null,
                null,
                userDto,
                ubicacionDto,
                TipoCliente.NATURAL,
                null
        );
    }

    @Test
    void registrarCliente_DeberiaEnviarEmailYRetornar200() throws Exception {
        // 🚀 Ejecutamos la petición REAL al controlador, usando el servicio real (incluye email)
        mockMvc.perform(post("/api/clienteBanner/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro logrado exitosamente."))
                .andExpect(jsonPath("$.error").value(false));
    }
}

