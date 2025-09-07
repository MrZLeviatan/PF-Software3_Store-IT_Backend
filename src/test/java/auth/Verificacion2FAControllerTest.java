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

@SpringBootTest(classes = Main.class)       // Levantamos el test con todo y Spring
@AutoConfigureMockMvc(addFilters = false)  // Ignora seguridad para pruebas
@Transactional
public class Verificacion2FAControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepo clienteRepo;

    private VerificacionCodigoDto verificacionCodigoDto;

    private Cliente clienteBase;


    @BeforeEach
    void setUp() {
        // Caso válido (email + código correctos)
        verificacionCodigoDto = new VerificacionCodigoDto(
                "cliente@test.com",   // debes asegurarte que exista en la BD en estado INACTIVA
                "55B83C"
        );

        // Crear un cliente reutilizable para todos los tests
        clienteBase = new Cliente();
        clienteBase.setNombre("Juan");
        clienteBase.setTelefono("9999999999");
        clienteBase.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        clienteBase.setUser(user);

        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigo.setClave("55B83C");
        codigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        clienteBase.getUser().setCodigo(codigo);

        clienteRepo.save(clienteBase);
    }


    @Test
    void verificarLogin_Exitoso_DeberiaRetornar200() throws  Exception {

        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.mensaje.token").exists()); // token present

    }


    @Test
    void verificarLogin_EmailNoExiste_DeberiaRetornar404() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto("noexiste@test.com", "123456");

        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(true));
    }



    @Test
    void verificarLogin_CodigoIncorrecto_DeberiaRetornar401() throws  Exception {

        clienteBase.getUser().getCodigo().setClave("345678");
        clienteRepo.save(clienteBase);

        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("El código proporcionado no coincide")); // ejemplo

    }


    @Test
    void verificarLogin_CodigoExpirado_DeberiaRetornar410() throws  Exception {

        clienteBase.getUser().getCodigo().setFechaExpiracion(LocalDateTime.now().minusMinutes(15));
        clienteRepo.save(clienteBase);

        mockMvc.perform(post("/api/auth/login-verificación")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").value("El código proporcionado a expirado")); // ejemplo

    }

}
