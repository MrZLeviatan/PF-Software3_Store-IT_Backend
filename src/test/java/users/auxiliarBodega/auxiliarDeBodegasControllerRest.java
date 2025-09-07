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

class AuxiliarBodegaControllerTest {

    @Mock
    private AuxiliarBodegaService auxiliarBodegaService;

    @InjectMocks
    private AuxiliarBodegaController auxiliarBodegaController;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    // --- Tests for 'agregarCantidadProducto' ---
    @Test
    void testAgregarCantidadProducto_Success() throws ElementoNulosException, ElementoNoEncontradoException {
        // Arrange
        RegistrarProductoExistenteDto requestDto = new RegistrarProductoExistenteDto("P-001", 5, TipoProducto.FRAGIL, "email", "desc");
        // The service method is 'void', so we just ensure it runs without throwing an exception
        doNothing().when(auxiliarBodegaService).AgregarCantidadProducto(requestDto);

        // Act
        ResponseEntity<MensajeDto<String>> response = auxiliarBodegaController.agregarCantidadProducto(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MensajeDto<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.error());
        assertEquals("Cantidad agregada correctamente. Pendiente de autorización.", body.mensaje());

        // Verify the service method was called exactly once with the correct DTO
        verify(auxiliarBodegaService, times(1)).AgregarCantidadProducto(requestDto);
    }

    @Test
    void testAgregarCantidadProducto_ProductoNoEncontrado() throws ElementoNulosException, ElementoNoEncontradoException {
        // Arrange
        RegistrarProductoExistenteDto requestDto = new RegistrarProductoExistenteDto("P-001", 5, TipoProducto.FRAGIL, "email22", "desripc");
        // Simulate the service throwing an exception
        doThrow(new ElementoNoEncontradoException("Producto no encontrado")).when(auxiliarBodegaService).AgregarCantidadProducto(requestDto);

        // Act & Assert
        assertThrows(ElementoNoEncontradoException.class, () -> {
            auxiliarBodegaController.agregarCantidadProducto(requestDto);
        });

        // Verify the service was called
        verify(auxiliarBodegaService, times(1)).AgregarCantidadProducto(requestDto);
    }

    // --- Tests for 'verDetalleProducto' ---
    @Test
    void testVerDetalleProducto_Success() throws ElementoNoEncontradoException {
        // Arrange
        String codigo = "P-001";
        ProductoDto producto = new ProductoDto(1L, codigo, "Laptop", 10, "img.jpg", "Laptop de 14 pulgadas", TipoProducto.FRAGIL, EstadoProducto.EN_BODEGA, "BOD-1");
        when(auxiliarBodegaService.verDetalleProducto(codigo)).thenReturn(producto);

        // Act
        ResponseEntity<MensajeDto<ProductoDto>> response = auxiliarBodegaController.verDetalleProducto(codigo);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MensajeDto<ProductoDto> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.error());
        assertEquals(codigo, body.mensaje().codigoProducto());

        // Verify the service method was called
        verify(auxiliarBodegaService, times(1)).verDetalleProducto(codigo);
    }

    @Test
    void testVerDetalleProducto_NoEncontrado() throws ElementoNoEncontradoException {
        // Arrange
        String codigoInvalido = "P-999";
        when(auxiliarBodegaService.verDetalleProducto(codigoInvalido)).thenThrow(new ElementoNoEncontradoException("Producto no encontrado"));

        // Act & Assert
        assertThrows(ElementoNoEncontradoException.class, () -> {
            auxiliarBodegaController.verDetalleProducto(codigoInvalido);
        });

        // Verify the service was called
        verify(auxiliarBodegaService, times(1)).verDetalleProducto(codigoInvalido);
    }

    // --- Tests for 'listarProductos' ---
    @Test
    void testListarProductos_Success() {
        // Arrange
        List<ProductoDto> listaProductos = Collections.singletonList(
                new ProductoDto(1L, "P-001", "Laptop", 10, "img.jpg", "Descripción", TipoProducto.FRAGIL, EstadoProducto.EN_BODEGA, "BOD-1")
        );
        when(auxiliarBodegaService.listarProductos(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(listaProductos);

        // Act
        ResponseEntity<MensajeDto<List<ProductoDto>>> response = auxiliarBodegaController.listarProductos(null, null, null, null, 0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MensajeDto<List<ProductoDto>> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.error());
        assertEquals(1, body.mensaje().size());
        assertEquals("Laptop", body.mensaje().get(0).nombre());

        // Verify the service method was called
        verify(auxiliarBodegaService, times(1)).listarProductos(null, null, null, null, 0, 10);
    }

    // --- Tests for 'registrarNuevoProducto' ---
    @Test
    void testRegistrarNuevoProducto_Success() throws ElementoRepetidoException, ElementoNulosException, ElementoNoEncontradoException, IOException {
        // Arrange
        // Create a mock MultipartFile to simulate the file upload
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",                    // The name of the request parameter
                "imagen.jpg",              // Original file name
                "image/jpeg",              // Content type
                "Esto es el contenido del archivo".getBytes() // File content as bytes
        );

        // Create the DTO with the mock file
        RegistroNuevoProductoDto requestDto = new RegistroNuevoProductoDto(
                "P-002",
                "Monitor",
                32,
                mockFile,                  // Pass the mock file here
                "Monitor",
                TipoProducto.ESTANDAR,
                "20",
                "BOD-2",
                "email@test.com"
        );

        // Mock the service call
        doNothing().when(auxiliarBodegaService).RegistroNuevoProducto(any(RegistroNuevoProductoDto.class));

        // Act
        ResponseEntity<MensajeDto<String>> response = auxiliarBodegaController.registrarNuevoProducto(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MensajeDto<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.error());
        assertEquals("Producto registrado correctamente. Pendiente de autorización", body.mensaje());

        // Verify the service method was called
        verify(auxiliarBodegaService, times(1)).RegistroNuevoProducto(requestDto);
    }

    // --- Tests for 'retiroProducto' ---
    @Test
    void testRetiroProducto_Success() throws ElementoNoValidoException, ElementoNoEncontradoException {
        // Arrange
        RetiroProductoDto requestDto = new RetiroProductoDto("P-001", 5, "email@test.com", "descripcion de esto");
        doNothing().when(auxiliarBodegaService).RetiroProducto(requestDto);

        // Act
        ResponseEntity<MensajeDto<String>> response = auxiliarBodegaController.retiroProducto(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MensajeDto<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.error());
        assertEquals("Retiro registrado correctamente.", body.mensaje());

        // Verify the service method was called
        verify(auxiliarBodegaService, times(1)).RetiroProducto(requestDto);
    }
}