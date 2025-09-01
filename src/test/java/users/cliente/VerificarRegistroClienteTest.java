package users.cliente;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
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

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class VerificarRegistroClienteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private VerificacionCodigoDto verificacionCodigoDto;

    @BeforeEach
    void setUp() {
        // Caso válido (email + código correctos)
        verificacionCodigoDto = new VerificacionCodigoDto(
                "nikis281002@gmail.com",   // debes asegurarte que exista en la BD en estado INACTIVA
                "55B83C"
        );
    }

    // ✅ Caso exitoso
    @Test
    void verificarCliente_DeberiaRetornar200YActivarCuenta() throws Exception {
        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificacionCodigoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cliente verificado con éxito."))
                .andExpect(jsonPath("$.error").value(false));
    }

    // ❌ Caso 1: Email no encontrado
    @Test
    void verificarCliente_EmailNoExiste_DeberiaRetornar404() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto(
                "no_existe@test.com",
                "XYZ789"
        );

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Persona no encontrada o no registrada"))
                .andExpect(jsonPath("$.error").value(true));
    }

    // ❌ Caso 2: Código inválido
    @Test
    void verificarCliente_CodigoInvalido_DeberiaRetornar400() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto(
                "cliente_valido@test.com",
                "CODIGO_ERRONEO"
        );

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El código de verificación es incorrecto o ha expirado"))
                .andExpect(jsonPath("$.error").value(true));
    }

    // ❌ Caso 3: Cliente ya activado
    @Test
    void verificarCliente_CuentaYaActiva_DeberiaRetornar400() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto(
                "cliente_activo@test.com", // este usuario debe existir en BD ya con EstadoCuenta.ACTIVO
                "ABC123"
        );

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("La cuenta ya fue activada anteriormente"))
                .andExpect(jsonPath("$.error").value(true));
    }

    // ❌ Caso 4: Validación de @Valid (email inválido)
    @Test
    void verificarCliente_EmailInvalido_DeberiaRetornar400() throws Exception {
        VerificacionCodigoDto dto = new VerificacionCodigoDto(
                "email_invalido", // formato incorrecto
                "ABC123"
        );

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists())
                .andExpect(jsonPath("$.error").value(true));
    }
}

