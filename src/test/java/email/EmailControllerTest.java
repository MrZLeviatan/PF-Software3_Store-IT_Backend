package email;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.model.enums.TipoCliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de integración para el EmailController de Store-IT.

  Estos tests validan la correcta interacción del sistema con:
  - Registro de cliente tradicional.
  - Registro de cliente mediante Google.
  - Verificación de cliente con código enviado por email.

   Se usan componentes reales:
   SpringBootTest para levantar el contexto completo.
   MockMvc para simular peticiones HTTP.
   ObjectMapper para enviar DTOs como JSON.
  Transaccionalidad para revertir cambios después de cada test.
 */
@SpringBootTest(classes = Main.class)       // Levantamos el test con todo y Spring
@AutoConfigureMockMvc(addFilters = false)  // ignora seguridad
@Transactional
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos en JSON

    // DTOs usados en los distintos escenarios de prueba
    private CrearClienteDto crearClienteDto;
    private CrearClienteGoogleDto crearClienteGoogleDto;
    private VerificacionCodigoDto verificacionCodigoDto;

    // Inicializamos datos comunes antes de cada test
    @BeforeEach
    void setUp() {
        CrearUserDto userDto = new CrearUserDto("nikis281002@gmail.com", "Password123");
        UbicacionDto ubicacionDto = new UbicacionDto("Colombia", "Medellín", 6.25184, -75.56359);

        crearClienteDto = new CrearClienteDto(
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

        crearClienteGoogleDto = new CrearClienteGoogleDto(
                "Juan Pérez",
                "3001234567",
                "CO",
                null,
                null,
                "nicolas.cabreras@uqvirtual.edu.co",
                "Password123",
                ubicacionDto
        );

        verificacionCodigoDto = new VerificacionCodigoDto(
                "nikis281002@gmail.com",
                "2BF6E0"
        );
    }

    //  Registro tradicional de cliente
    @Test
    void registrarCliente_DeberiaEnviarEmailYRetornar200() throws Exception {
        // Ejecuta la petición real al endpoint de registro
        mockMvc.perform(post("/api/store-it/registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro logrado exitosamente."))
                .andExpect(jsonPath("$.error").value(false));
    }

    //  Verificación de cliente con código enviado por correo
    @Test
    void verificarCliente_DeberiaRetornar200YMensajeExito() throws Exception {
        // Ejecuta la petición real al endpoint de verificación
        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cliente verificado con éxito."))
                .andExpect(jsonPath("$.error").value(false));
    }

    //  Registro de cliente mediante Google
    @Test
    void registroClienteGoogle_DeberiaEnviarEmailYRetornar200() throws Exception {
        // Ejecuta la petición real al endpoint de registro con Google
        mockMvc.perform(post("/api/store-it/registro-clientes-google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearClienteGoogleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Registro logrado exitosamente."))
                .andExpect(jsonPath("$.error").value(false));
    }
}
