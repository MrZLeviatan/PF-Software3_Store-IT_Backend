package users.cliente;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
import co.edu.uniquindio.model.enums.TipoCodigo;
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


import java.time.LocalDateTime;

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

    @Autowired
    private ClienteRepo clienteRepo;

    @BeforeEach
    void setUp() {
        // Caso válido (email + código correctos)
        verificacionCodigoDto = new VerificacionCodigoDto(
                "cliente@test.com",   // debes asegurarte que exista en la BD en estado INACTIVA
                "55B83C"
        );
    }

    // ✅ Caso exitoso
    @Test
    void verificarCliente_DeberiaRetornar200YActivarCuenta() throws Exception {

        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setTelefono("9999999999");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.INACTIVA);
        cliente.setUser(user);

        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigo.setClave("55B83C");
        codigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        cliente.getUser().setCodigo(codigo);
        clienteRepo.save(cliente);

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
    void verificarCliente_CodigoInvalido_DeberiaRetornar418() throws Exception {

        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setTelefono("9999999999");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.INACTIVA);
        cliente.setUser(user);

        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigo.setClave("55B83C");
        codigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        cliente.getUser().setCodigo(codigo);
        clienteRepo.save(cliente);

        VerificacionCodigoDto dto = new VerificacionCodigoDto(
                "cliente@test.com",
                "CODIGO_ERRONEO"
        );

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("El código proporcionado no coincide"))
                .andExpect(jsonPath("$.error").value(true));
    }


    @Test
    void verificarCliente_CodigoExpirado_DeberiaRetornar418() throws Exception {

        // Creamos cliente con estado Activado manualmente en repo
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setTelefono("9999999999");
        cliente.setTipoCliente(TipoCliente.NATURAL);

        User user = new User();
        user.setEmail("cliente@test.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.INACTIVA);
        cliente.setUser(user);

        Codigo codigo = new Codigo();
        codigo.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigo.setClave("123456");
        codigo.setFechaExpiracion(LocalDateTime.now().minusMinutes(20));
        cliente.getUser().setCodigo(codigo);
        clienteRepo.save(cliente);

        VerificacionCodigoDto dto = new VerificacionCodigoDto(
                "cliente@test.com",
                "CODIGO_ERRONEO"
        );

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("El código proporcionado a expirado"))
                .andExpect(jsonPath("$.error").value(true));

    }


    // ❌ Caso 3: Cliente ya activado
    @Test
    void verificarCliente_CuentaYaActiva_DeberiaRetornar400() throws Exception {

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

        VerificacionCodigoDto dto = new VerificacionCodigoDto(
                "cliente@test.com", // este usuario debe existir en BD ya con EstadoCuenta.ACTIVO
                "ABC123"
        );

        mockMvc.perform(post("/api/store-it/verificar-registro-clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
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

