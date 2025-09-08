package users.gestorBodega;

import co.edu.uniquindio.controller.personalBodega.GestorBodegaController;
import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.dto.objects.producto.AutorizacionProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.enums.EstadoMovimiento;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import co.edu.uniquindio.model.enums.TipoProducto;
import co.edu.uniquindio.service.users.GestorBodegasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * Clase de pruebas unitarias para GestorBodegaController.
 *
 * Estas pruebas validan la lógica del controlador encargado de la gestión de productos
 * y movimientos en la bodega (rol de Gestor de Bodega).
 *
 * Estrategia:
 * - Se usa Mockito para simular el comportamiento del servicio GestorBodegasService.
 * - Se prueban diferentes casos de uso: listar productos, autorizar movimientos,
 *   obtener movimientos, ver detalles de productos y validar excepciones.
 */
class GestorBodegaControllerTest {

    // Mock del servicio de negocio que maneja la lógica de los gestores de bodega
    @Mock
    private GestorBodegasService gestorBodegasService;

    // Inyectamos el controlador con el servicio simulado
    @InjectMocks
    private GestorBodegaController gestorBodegaController;

    // Inicializamos los mocks antes de cada prueba
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //  Prueba listar productos disponibles en la bodega
    @Test
    void testListarProductos() {
        // Se crea un producto simulado
        ProductoDto producto = new ProductoDto(
                1L, "P-001", "Laptop", 10,
                "imagen.jpg", "Laptop de 14 pulgadas",
                TipoProducto.FRAGIL, EstadoProducto.EN_BODEGA, "Bodega1"
        );

        // Configuramos el mock para que devuelva el producto
        when(gestorBodegasService.listarProductos(
                null, null, null, null, 0, 10
        )).thenReturn(List.of(producto));

        // Llamamos al método del controlador
        var response = gestorBodegaController.listarProductos(
                null, null, null, null, 0, 10
        );

        // Validaciones
        var mensajeDto = response.getBody();
        var resultado = mensajeDto.mensaje();

        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).nombre());
        assertEquals("P-001", resultado.get(0).codigoProducto());
    }

    // Prueba autorizar un movimiento de producto
    @Test
    void testAutorizarMovimiento() throws Exception {
        AutorizacionProductoDto dto = new AutorizacionProductoDto(
                "codigo123", "mov001", "gestor@test.com",
                "Autorizado correctamente", EstadoMovimiento.VERIFICADO
        );

        // Simulamos que el servicio no hace nada (se ejecuta correctamente)
        doNothing().when(gestorBodegasService).autorizarMovimiento(any(AutorizacionProductoDto.class));

        // Llamada al controlador
        MensajeDto<String> resultado = gestorBodegaController.autorizarMovimiento(dto).getBody();

        // Verificaciones
        assertEquals("Movimiento verificado correctamente.", resultado.mensaje());
        assertFalse(resultado.error());
        verify(gestorBodegasService, times(1)).autorizarMovimiento(any(AutorizacionProductoDto.class));
    }

    // Prueba listar movimientos de productos en la bodega
    @Test
    void testListarMovimientos() {
        MovimientosProductoDto movimiento = new MovimientosProductoDto(
                1L, "Ingreso Laptop", TipoMovimiento.INGRESO,
                LocalDateTime.now(), LocalDateTime.now(),
                "gestor@test.com", "codigo123", "juan@test.com", "gestor@test.com"
        );

        when(gestorBodegasService.obtenerMovimientosProducto(
                anyString(), any(), any(), anyString(), anyString(), anyString(), anyInt(), anyInt()
        )).thenReturn(List.of(movimiento));

        ResponseEntity<MensajeDto<List<MovimientosProductoDto>>> resp =
                gestorBodegaController.obtenerMovimientosProducto(
                        "codigo123", TipoMovimiento.INGRESO, LocalDateTime.now(),
                        "juan@test.com", "gestor@test.com", "bodega1", 0, 10
                );

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertFalse(resp.getBody().error());
        assertEquals("Ingreso Laptop", resp.getBody().mensaje().get(0).descripcion());
    }

    //  Prueba ver detalles de un producto específico
    @Test
    void testVerDetalleProducto() throws ElementoNoEncontradoException {
        ProductoDto producto = new ProductoDto(
                1L, "P-001", "Laptop Lenovo", 10, "imagen.jpg",
                "Laptop de 14 pulgadas", TipoProducto.FRAGIL,
                EstadoProducto.EN_BODEGA, "BOD-1"
        );

        when(gestorBodegasService.verDetalleProducto("P-001")).thenReturn(producto);

        ResponseEntity<MensajeDto<ProductoDto>> response =
                gestorBodegaController.verDetalleProducto("P-001");

        MensajeDto<ProductoDto> body = response.getBody();
        assertFalse(body.error());
        assertEquals("Laptop Lenovo", body.mensaje().nombre());
    }

    //  Prueba ver detalles de un movimiento específico
    @Test
    void testVerDetallesMovimiento() throws ElementoNoEncontradoException {
        MovimientosProductoDto movimiento = new MovimientosProductoDto(
                1L, "Ingreso Laptop", TipoMovimiento.INGRESO,
                LocalDateTime.now(), LocalDateTime.now(),
                "gestor@test.com", "codigo123", "juan@test.com", "gestor@test.com"
        );

        when(gestorBodegasService.verDetallesMovimiento(1L)).thenReturn(movimiento);

        ResponseEntity<MensajeDto<MovimientosProductoDto>> response =
                gestorBodegaController.verDetallesMovimiento(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ingreso Laptop", response.getBody().mensaje().descripcion());
        verify(gestorBodegasService, times(1)).verDetallesMovimiento(1L);
    }

    //  Prueba obtener lista de movimientos (caso genérico)
    @Test
    void testObtenerMovimientosProducto() {
        MovimientosProductoDto movimiento = new MovimientosProductoDto(
                1L, "Salida Laptop", TipoMovimiento.RETIRO,
                LocalDateTime.now(), LocalDateTime.now(),
                "gestor@test.com", "codigo123", "juan@test.com", "gestor@test.com"
        );

        when(gestorBodegasService.obtenerMovimientosProducto(
                any(), any(), any(), any(), any(), any(), anyInt(), anyInt()
        )).thenReturn(List.of(movimiento));

        ResponseEntity<MensajeDto<List<MovimientosProductoDto>>> response =
                gestorBodegaController.obtenerMovimientosProducto(null, null, null, null, null, null, 0, 10);

        assertEquals("Salida Laptop", response.getBody().mensaje().get(0).descripcion());
        assertEquals(TipoMovimiento.RETIRO, response.getBody().mensaje().get(0).tipoMovimiento());
    }

    //  Caso de producto no encontrado
    @Test
    void testVerDetalleProducto_ProductoNoEncontrado() throws ElementoNoEncontradoException {
        String codigoProducto = "codigoInvalido";

        when(gestorBodegasService.verDetalleProducto(codigoProducto))
                .thenThrow(new ElementoNoEncontradoException("Producto no encontrado"));

        assertThrows(ElementoNoEncontradoException.class, () -> {
            gestorBodegaController.verDetalleProducto(codigoProducto);
        });

        verify(gestorBodegasService, times(1)).verDetalleProducto(codigoProducto);
    }
}
