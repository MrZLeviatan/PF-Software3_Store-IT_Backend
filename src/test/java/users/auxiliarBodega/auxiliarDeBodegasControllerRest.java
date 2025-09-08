package users.auxiliarBodega;

import co.edu.uniquindio.controller.personalBodega.AuxiliarBodegaController;
import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.producto.RegistrarProductoExistenteDto;
import co.edu.uniquindio.dto.objects.producto.RegistroNuevoProductoDto;
import co.edu.uniquindio.dto.objects.producto.RetiroProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValidoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;
import co.edu.uniquindio.service.users.AuxiliarBodegaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuxiliarBodegaController
 *
 * Verifica las operaciones que puede realizar un auxiliar de bodega en Store-IT:
 * - Registrar productos nuevos
 * - Agregar cantidad a productos existentes
 * - Retirar productos
 * - Listar y consultar detalles de productos
 */
class AuxiliarBodegaControllerTest {

    // Simulación del servicio (mock)
    @Mock
    private AuxiliarBodegaService auxiliarBodegaService;

    // Controlador que se va a probar, con el mock inyectado
    @InjectMocks
    private AuxiliarBodegaController auxiliarBodegaController;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
    }

    // --- Pruebas para agregar cantidad a un producto existente ---
    @Test
    void testAgregarCantidadProducto_Success() throws ElementoNulosException, ElementoNoEncontradoException {
        RegistrarProductoExistenteDto requestDto = new RegistrarProductoExistenteDto("P-001", 5, TipoProducto.FRAGIL, "email", "desc");
        doNothing().when(auxiliarBodegaService).AgregarCantidadProducto(requestDto);

        ResponseEntity<MensajeDto<String>> response = auxiliarBodegaController.agregarCantidadProducto(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cantidad agregada correctamente. Pendiente de autorización.", response.getBody().mensaje());

        verify(auxiliarBodegaService, times(1)).AgregarCantidadProducto(requestDto);
    }

    @Test
    void testAgregarCantidadProducto_ProductoNoEncontrado() throws ElementoNulosException, ElementoNoEncontradoException {
        RegistrarProductoExistenteDto requestDto = new RegistrarProductoExistenteDto("P-001", 5, TipoProducto.FRAGIL, "email22", "desripc");
        doThrow(new ElementoNoEncontradoException("Producto no encontrado")).when(auxiliarBodegaService).AgregarCantidadProducto(requestDto);

        assertThrows(ElementoNoEncontradoException.class, () -> {
            auxiliarBodegaController.agregarCantidadProducto(requestDto);
        });

        verify(auxiliarBodegaService, times(1)).AgregarCantidadProducto(requestDto);
    }

    // --- Pruebas para ver detalle de un producto ---
    @Test
    void testVerDetalleProducto_Success() throws ElementoNoEncontradoException {
        String codigo = "P-001";
        ProductoDto producto = new ProductoDto(1L, codigo, "Laptop", 10, "img.jpg", "Laptop de 14 pulgadas", TipoProducto.FRAGIL, EstadoProducto.EN_BODEGA, "BOD-1");
        when(auxiliarBodegaService.verDetalleProducto(codigo)).thenReturn(producto);

        ResponseEntity<MensajeDto<ProductoDto>> response = auxiliarBodegaController.verDetalleProducto(codigo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(codigo, response.getBody().mensaje().codigoProducto());

        verify(auxiliarBodegaService, times(1)).verDetalleProducto(codigo);
    }

    @Test
    void testVerDetalleProducto_NoEncontrado() throws ElementoNoEncontradoException {
        String codigoInvalido = "P-999";
        when(auxiliarBodegaService.verDetalleProducto(codigoInvalido)).thenThrow(new ElementoNoEncontradoException("Producto no encontrado"));

        assertThrows(ElementoNoEncontradoException.class, () -> {
            auxiliarBodegaController.verDetalleProducto(codigoInvalido);
        });

        verify(auxiliarBodegaService, times(1)).verDetalleProducto(codigoInvalido);
    }

    // --- Pruebas para listar productos ---
    @Test
    void testListarProductos_Success() {
        List<ProductoDto> listaProductos = Collections.singletonList(
                new ProductoDto(1L, "P-001", "Laptop", 10, "img.jpg", "Descripción", TipoProducto.FRAGIL, EstadoProducto.EN_BODEGA, "BOD-1")
        );
        when(auxiliarBodegaService.listarProductos(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(listaProductos);

        ResponseEntity<MensajeDto<List<ProductoDto>>> response = auxiliarBodegaController.listarProductos(null, null, null, null, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().mensaje().size());

        verify(auxiliarBodegaService, times(1)).listarProductos(null, null, null, null, 0, 10);
    }

    // --- Pruebas para registrar un producto nuevo ---
    @Test
    void testRegistrarNuevoProducto_Success() throws ElementoRepetidoException, ElementoNulosException, ElementoNoEncontradoException, IOException, ElementoNoValidoException {
        // Archivo simulado para el producto
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "imagen.jpg",
                "image/jpeg",
                "Esto es el contenido del archivo".getBytes()
        );

        RegistroNuevoProductoDto requestDto = new RegistroNuevoProductoDto(
                "P-002",
                "Monitor",
                32,
                mockFile,
                "Monitor",
                TipoProducto.ESTANDAR,
                "20",
                "BOD-2",
                "email@test.com"
        );

        doNothing().when(auxiliarBodegaService).RegistroNuevoProducto(any(RegistroNuevoProductoDto.class));

        ResponseEntity<MensajeDto<String>> response = auxiliarBodegaController.registrarNuevoProducto(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Producto registrado correctamente. Pendiente de autorización", response.getBody().mensaje());

        verify(auxiliarBodegaService, times(1)).RegistroNuevoProducto(requestDto);
    }

    // --- Pruebas para retirar un producto ---
    @Test
    void testRetiroProducto_Success() throws ElementoNoValidoException, ElementoNoEncontradoException {
        RetiroProductoDto requestDto = new RetiroProductoDto("P-001", 5, "email@test.com", "descripcion de esto");
        doNothing().when(auxiliarBodegaService).RetiroProducto(requestDto);

        ResponseEntity<MensajeDto<String>> response = auxiliarBodegaController.retiroProducto(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Retiro registrado correctamente.", response.getBody().mensaje());

        verify(auxiliarBodegaService, times(1)).RetiroProducto(requestDto);
    }
}
