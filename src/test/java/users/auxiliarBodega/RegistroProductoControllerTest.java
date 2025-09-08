package users.auxiliarBodega;

import co.edu.uniquindio.Main;
import co.edu.uniquindio.dto.objects.producto.RegistroNuevoProductoDto;
import co.edu.uniquindio.model.embeddable.DatosLaborales;
import co.edu.uniquindio.model.embeddable.Ubicacion;
import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.objects.Bodega;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.*;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.repository.objects.ProductoRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el registro de productos
 * en el módulo de Auxiliar de Bodega de Store-IT.
 *
 * Se valida:
 * - Caso exitoso (producto válido)
 * - Validaciones de campos obligatorios
 * - Persistencia en la base de datos
 */
@SpringBootTest(classes = Main.class) // Levanta todo el contexto de Spring
@AutoConfigureMockMvc(addFilters = false) // Desactiva seguridad en pruebas
@Transactional // Limpia cambios después de cada test
public class RegistroProductoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Cliente simulado para pruebas HTTP

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private BodegaRepo bodegaRepo;

    @Autowired
    private PersonalBodegaRepo personalBodegaRepo;

    // --- Métodos helper para armar datos válidos ---
    private Bodega crearBodegaValida() {
        Bodega bodega = new Bodega();
        Ubicacion ubicacion = new Ubicacion("Colombia", "Medellín", 6.25, -75.56);
        bodega.setUbicacion(ubicacion);
        bodega.setDireccion("Calle 123 #45-67");
        bodega.setTelefono("3001234567");
        return bodega;
    }

    private PersonalBodega crearPersonalBodegaValido() {
        // Datos laborales simulados
        DatosLaborales datosLaborales = new DatosLaborales();
        datosLaborales.setFechaInicioContrato(LocalDate.now().minusMonths(1));
        datosLaborales.setFechaFinContrato(null); // Contrato activo
        datosLaborales.setSueldo(new BigDecimal("2500000"));
        datosLaborales.setTipoContrato(TipoContrato.INDEFINIDO);
        datosLaborales.setEstadoContratoLaboral(EstadoContratoLaboral.ACTIVO);

        // Usuario embebido
        User user = new User();
        user.setEmail("auxiliar@bodega.com");
        user.setPassword("Password123");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);

        // Personal de bodega
        PersonalBodega personal = new PersonalBodega();
        personal.setNombre("Juan Auxiliar");
        personal.setTelefono("3101234567");
        personal.setDatosLaborales(datosLaborales);
        personal.setUser(user);
        personal.setTipoPersonalBodega(TipoPersonalBodega.AUXILIAR_BODEGA);

        return personalBodegaRepo.save(personal); // Se guarda en BD
    }

    // DTO válido para pruebas
    private RegistroNuevoProductoDto crearProductoValido(Bodega bodega, PersonalBodega personalBodega) {
        return new RegistroNuevoProductoDto(
                "PROD001",
                "Leche Entera",
                10,
                null, // archivo opcional
                "Leche fresca en caja",
                TipoProducto.ESTANDAR,
                bodega.getId().toString(),
                personalBodega.getUser().getEmail(),
                "Ingreso inicial de stock"
        );
    }

    // ✅ Caso exitoso: registrar producto válido
    @Test
    void registrarNuevoProducto_DeberiaRetornar200YMensajeExito() throws Exception {
        Bodega bodega = crearBodegaValida();
        bodegaRepo.save(bodega);

        PersonalBodega personal = crearPersonalBodegaValido();
        personalBodegaRepo.save(personal);

        RegistroNuevoProductoDto dto = crearProductoValido(bodega, personal);

        // Imagen simulada
        File file = new File("src/test/resources/imagenProducto.jpg");
        MockMultipartFile imagen = new MockMultipartFile(
                "imagenProducto",
                file.getName(),
                "image/jpeg",
                new FileInputStream(file)
        );

        mockMvc.perform(multipart("/api/auxiliar-bodega/productos")
                        .file(imagen)
                        .param("codigoProducto", dto.codigoProducto())
                        .param("nombre", dto.nombre())
                        .param("cantidad", String.valueOf(dto.cantidad()))
                        .param("descripcion", dto.descripcion())
                        .param("tipoProducto", dto.tipoProducto().name())
                        .param("idBodega", dto.idBodega())
                        .param("emailPersonalBodega", dto.emailPersonalBodega())
                        .param("descripcionMovimiento", dto.descripcionMovimiento() != null ? dto.descripcionMovimiento() : "")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Producto registrado correctamente. Pendiente de autorización"))
                .andExpect(jsonPath("$.error").value(false));

        // Verificar que se guardó en la base
        Producto productoGuardado = productoRepo.findByCodigoProducto(dto.codigoProducto()).orElseThrow();
        assertEquals(dto.nombre(), productoGuardado.getNombre());
    }

    // ❌ Nombre vacío -> error de validación
    @Test
    void registrarNuevoProducto_NombreVacio_DeberiaRetornar400() throws Exception {
        RegistroNuevoProductoDto dto = new RegistroNuevoProductoDto(
                "PROD002",
                "", // nombre vacío
                5,
                null,
                "Descripción prueba",
                TipoProducto.ESTANDAR,
                "1",
                "auxiliar@test.com",
                "Ingreso"
        );

        mockMvc.perform(multipart("/api/auxiliar-bodega/productos")
                        .file(new MockMultipartFile("imagenProducto", new byte[0]))
                        .param("codigoProducto", dto.codigoProducto())
                        .param("nombre", dto.nombre())
                        .param("cantidad", String.valueOf(dto.cantidad()))
                        .param("descripcion", dto.descripcion())
                        .param("tipoProducto", dto.tipoProducto().name())
                        .param("idBodega", dto.idBodega())
                        .param("emailPersonalBodega", dto.emailPersonalBodega())
                        .param("descripcionMovimiento", dto.descripcionMovimiento() != null ? dto.descripcionMovimiento() : "")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").exists());
    }

    // ❌ Cantidad nula -> error de validación
    @Test
    void registrarNuevoProducto_CantidadNula_DeberiaRetornar400() throws Exception {
        RegistroNuevoProductoDto dto = new RegistroNuevoProductoDto(
                "PROD003",
                "Yogurt",
                null, // cantidad nula
                null,
                "Yogurt natural",
                TipoProducto.FRAGIL,
                "1",
                "auxiliar@test.com",
                "Ingreso"
        );

        mockMvc.perform(multipart("/api/auxiliar-bodega/productos")
                        .file(new MockMultipartFile("imagenProducto", new byte[0]))
                        .param("codigoProducto", dto.codigoProducto())
                        .param("nombre", dto.nombre())
                        .param("descripcion", dto.descripcion())
                        .param("tipoProducto", dto.tipoProducto().name())
                        .param("idBodega", dto.idBodega())
                        .param("emailPersonalBodega", dto.emailPersonalBodega())
                        .param("descripcionMovimiento", dto.descripcionMovimiento() != null ? dto.descripcionMovimiento() : "")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.mensaje").exists());
    }
}
