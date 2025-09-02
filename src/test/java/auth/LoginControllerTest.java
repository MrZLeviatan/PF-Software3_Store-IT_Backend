package auth;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.auth.LoginDto;
import co.edu.uniquindio.repository.users.ClienteRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)       // Levantamos el test con todo y Spring
@AutoConfigureMockMvc(addFilters = false)  // Ignora seguridad para pruebas
@Transactional
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Convierte objetos en JSON

    @Autowired
    private ClienteRepo clienteRepo;

    @Autowired
    private  PasswordEncoder passwordEncoder;


    @Test
    void login_Exitoso_DeberiaRetornar200() throws Exception {
        // Creamos un cliente válido en BD
        Cliente cliente = new Cliente();
        cliente.setNombre("Carlos López");
        cliente.setTelefono("3023456789");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("login_test" + UUID.randomUUID() + "@test.com");
        user.setPassword(passwordEncoder.encode("Password123"));
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);

        cliente.setUser(user);
        clienteRepo.save(cliente);

        LoginDto loginDto = new LoginDto(user.getEmail(), "Password123");


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("En espera de verificación login"))
                .andExpect(jsonPath("$.error").value(false));
    }


    @Test
    void login_UsuarioPasswordNoCoincide_DeberiaRetornar401() throws Exception {

        // Creamos un cliente válido en BD
        Cliente cliente = new Cliente();
        cliente.setNombre("Carlos López");
        cliente.setTelefono("3023456789");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("login_test" + UUID.randomUUID() + "@test.com");
        user.setPassword(passwordEncoder.encode("Password123"));
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);

        cliente.setUser(user);
        clienteRepo.save(cliente);

        LoginDto loginDto = new LoginDto(user.getEmail(), "Password133");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("La contraseña proporcionada es incorrecta"))
                .andExpect(jsonPath("$.error").value(true));
    }


    @Test
    void login_EmailInvalido_DeberiaRetornar400() throws Exception {
        LoginDto loginDto = new LoginDto("correo_invalido", "Password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.error").value(true));
    }


    @Test
    void login_PasswordVacio_DeberiaRetornar400() throws Exception {
        LoginDto loginDto = new LoginDto("cliente@test.com", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.error").value(true));
    }


    @Test
    void login_UsuarioNoEncontrado_DeberiaRetornar404() throws Exception {
        LoginDto loginDto = new LoginDto("noexiste@test.com", "Password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Persona no encontrada o no registrada"))
                .andExpect(jsonPath("$.error").value(true));
    }

}
