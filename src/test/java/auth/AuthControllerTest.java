package auth;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.auth.LoginDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)       // Levantamos el test con todo y Spring
@AutoConfigureMockMvc(addFilters = false)  // ignora seguridad
public class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos en JSON / Converts objects to JSON

    private LoginDto loginDto;
    private VerificacionCodigoDto verificacionCodigoDto;

    @BeforeEach
    void setUp() {
        // Preparamos un loginDto válido / Prepare a valid loginDto
        loginDto = new LoginDto(
                "nikis281002@gmail.com",
                "Password123"
        );

        // Preparamos un código de verificación válido / Prepare a valid verification code
        verificacionCodigoDto = new VerificacionCodigoDto(
                "nikis281002@gmail.com",
                "D1B240"
        );
    }

    @Test
    void login_DeberiaRetornar200YMensajeEsperaVerificacion() throws Exception {
        // Ejecuta el POST real al endpoint /login
        // Executes the real POST request to /login
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("En espera de verificación login"))
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void verificarLogin_DeberiaRetornar200YTokenDto() throws Exception {
        // Ejecuta el POST real al endpoint /login-verificación
        // Executes the real POST request to /login-verificación
        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje").exists()) // Verifica que el tokenDto venga
                .andExpect(jsonPath("$.mensaje.token").isNotEmpty());
    }
}

