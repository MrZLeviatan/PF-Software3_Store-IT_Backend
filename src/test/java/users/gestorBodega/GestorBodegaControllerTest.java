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

class GestorBodegaControllerTest {

    @Mock
    private GestorBodegasService gestorBodegasService;

    @InjectMocks
    private GestorBodegaController gestorBodegaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarProductos() {
        // Crear producto de prueba
        ProductoDto producto = new ProductoDto(
                1L,                     // id
                "P-001",                // codigoProducto
                "Laptop",               // nombre
                10,                     // cantidad
                "imagen.jpg",           // imagen
                "Laptop de 14 pulgadas",// descripcion
                TipoProducto.FRAGIL,    // tipoProducto
                EstadoProducto.EN_BODEGA, // estadoProducto
                "Bodega1"               // idBodega
        );

        // Configuramos el mock para devolver una lista
        when(gestorBodegasService.listarProductos(
                null, null, null, null, 0, 10
        )).thenReturn(List.of(producto));

        // Llamamos al controlador → devuelve ResponseEntity<MensajeDto<List<ProductoDto>>>
        var response = gestorBodegaController.listarProductos(
                null, null, null, null, 0, 10
        );

        // Sacamos el body → MensajeDto<List<ProductoDto>>
        var mensajeDto = response.getBody();

        // Sacamos la lista de productos del "mensaje"
        var resultado = mensajeDto.mensaje();

        // Validamos
        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).nombre());
        assertEquals("P-001", resultado.get(0).codigoProducto());
        assertEquals(10, resultado.get(0).cantidad());
        assertEquals("Bodega1", resultado.get(0).idBodega());
    }

    @Test
    void testAutorizarMovimiento() throws Exception {
        AutorizacionProductoDto dto = new AutorizacionProductoDto(
                "codigo123",
                "mov001",
                "gestor@test.com",
                "Autorizado correctamente",
                EstadoMovimiento.VERIFICADO
        );

        // Simulamos el servicio: no hace nada cuando se llama
        doNothing().when(gestorBodegasService).autorizarMovimiento(any(AutorizacionProductoDto.class));

        // Ejecutamos el método del controller
        MensajeDto<String> resultado = gestorBodegaController.autorizarMovimiento(dto).getBody();

        // Verificamos la respuesta
        assertEquals("Movimiento verificado correctamente.", resultado.mensaje());
        assertFalse(resultado.error());

        // Verificamos que realmente se haya llamado al servicio
        verify(gestorBodegasService, times(1)).autorizarMovimiento(any(AutorizacionProductoDto.class));
    }

    @Test
    void testListarMovimientos() {
        MovimientosProductoDto movimiento = new MovimientosProductoDto(
                1L,
                "Ingreso Laptop",
                TipoMovimiento.INGRESO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "gestor@test.com",
                "codigo123",
                "juan@test.com",
                "gestor@test.com"
        );

        when(gestorBodegasService.obtenerMovimientosProducto(
                anyString(), any(), any(), anyString(), anyString(), anyString(), anyInt(), anyInt()
        )).thenReturn(List.of(movimiento));

        ResponseEntity<MensajeDto<List<MovimientosProductoDto>>> resp =
                gestorBodegaController.obtenerMovimientosProducto(
                        "codigo123",
                        TipoMovimiento.INGRESO,
                        LocalDateTime.now(),
                        "juan@test.com",
                        "gestor@test.com",
                        "bodega1",
                        0,
                        10
                );

        assertEquals(HttpStatus.OK, resp.getStatusCode());

        MensajeDto<List<MovimientosProductoDto>> body = resp.getBody();
        assertNotNull(body);
        assertFalse(body.error());

        List<MovimientosProductoDto> lista = body.mensaje();
        assertEquals(1, lista.size());
        assertEquals("Ingreso Laptop", lista.get(0).descripcion());
    }

    @Test
    void testVerDetalleProducto() throws ElementoNoEncontradoException {
        ProductoDto producto = new ProductoDto(
                1L,                      // id
                "P-001",                 // codigoProducto
                "Laptop Lenovo",         // nombre
                10,                      // cantidad
                "imagen.jpg",            // imagen
                "Laptop de 14 pulgadas", // descripcion
                TipoProducto.FRAGIL,     // tipoProducto
                EstadoProducto.EN_BODEGA,// estadoProducto
                "BOD-1"                  // idBodega
        );

        when(gestorBodegasService.verDetalleProducto("P-001")).thenReturn(producto);

        ResponseEntity<MensajeDto<ProductoDto>> response = gestorBodegaController.verDetalleProducto("P-001");

        // Validaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        MensajeDto<ProductoDto> body = response.getBody();
        assertFalse(body.error());

        ProductoDto dto = body.mensaje();
        assertEquals(1L, dto.id());
        assertEquals("P-001", dto.codigoProducto());
        assertEquals("Laptop Lenovo", dto.nombre());
        assertEquals(10, dto.cantidad());
        assertEquals("imagen.jpg", dto.imagen());
        assertEquals("Laptop de 14 pulgadas", dto.descripcion());
        assertEquals(TipoProducto.FRAGIL, dto.tipoProducto());
        assertEquals(EstadoProducto.EN_BODEGA, dto.estadoProducto());
        assertEquals("BOD-1", dto.idBodega());
    }

    @Test
    void testVerDetallesMovimiento() throws ElementoNoEncontradoException {
        // Creamos un movimiento de prueba
        MovimientosProductoDto movimiento = new MovimientosProductoDto(
                1L,                        // id
                "Ingreso Laptop",           // descripcion
                TipoMovimiento.INGRESO,     // tipoMovimiento
                LocalDateTime.now(),        // fechaMovimiento
                LocalDateTime.now(),        // fechaAutorizacion
                "gestor@test.com",          // emailPersonalResponsable
                "codigo123",                // codigoProducto
                "juan@test.com",            // emailPersonalAutorizado
                "gestor@test.com"           // emailPersonalVerificador
        );

        // Mock del servicio
        when(gestorBodegasService.verDetallesMovimiento(1L)).thenReturn(movimiento);

        // Llamada al controlador
        ResponseEntity<MensajeDto<MovimientosProductoDto>> response =
                gestorBodegaController.verDetallesMovimiento(1L);

        // Validaciones de ResponseEntity
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        MensajeDto<MovimientosProductoDto> mensajeDto = response.getBody();
        assertFalse(mensajeDto.error());

        // Validaciones de los datos dentro del DTO
        MovimientosProductoDto dto = mensajeDto.mensaje();
        assertEquals(1L, dto.id());
        assertEquals("Ingreso Laptop", dto.descripcion());
        assertEquals(TipoMovimiento.INGRESO, dto.tipoMovimiento());
        assertEquals("codigo123", dto.idProducto());

        // Verificación de llamada al servicio
        verify(gestorBodegasService, times(1)).verDetallesMovimiento(1L);
    }

    @Test
    void testObtenerMovimientosProducto() {
        // Creamos un movimiento de prueba
        MovimientosProductoDto movimiento = new MovimientosProductoDto(
                1L,                        // id
                "Salida Laptop",           // descripcion
                TipoMovimiento.RETIRO,     // tipoMovimiento
                LocalDateTime.now(),       // fechaMovimiento
                LocalDateTime.now(),       // fechaAutorizacion
                "gestor@test.com",         // emailPersonalResponsable
                "codigo123",               // codigoProducto
                "juan@test.com",           // emailPersonalAutorizado
                "gestor@test.com"          // emailPersonalVerificador
        );

        // Mock del servicio
        when(gestorBodegasService.obtenerMovimientosProducto(
                any(), any(), any(), any(), any(), any(), anyInt(), anyInt()
        )).thenReturn(List.of(movimiento));

        // Llamada al controlador
        ResponseEntity<MensajeDto<List<MovimientosProductoDto>>> response =
                gestorBodegaController.obtenerMovimientosProducto(
                        null, null, null, null, null, null, 0, 10
                );

        // Validaciones de ResponseEntity
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        MensajeDto<List<MovimientosProductoDto>> mensajeDto = response.getBody();
        assertFalse(mensajeDto.error());

        // Validaciones de los datos dentro de la lista
        List<MovimientosProductoDto> lista = mensajeDto.mensaje();
        assertEquals(1, lista.size());
        assertEquals("Salida Laptop", lista.get(0).descripcion());
        assertEquals(TipoMovimiento.RETIRO, lista.get(0).tipoMovimiento());

        // Verificación de llamada al servicio
        verify(gestorBodegasService, times(1)).obtenerMovimientosProducto(
                any(), any(), any(), any(), any(), any(), anyInt(), anyInt()
        );
    }

    @Test
    void testVerDetalleProducto_ProductoNoEncontrado() throws ElementoNoEncontradoException {
        // Arrange: simulamos que el servicio lanza la excepción
        String codigoProducto = "codigoInvalido";
        when(gestorBodegasService.verDetalleProducto(codigoProducto))
                .thenThrow(new ElementoNoEncontradoException("Producto no encontrado"));

        // Act & Assert: verificamos que la excepción se propaga
        assertThrows(ElementoNoEncontradoException.class, () -> {
            gestorBodegaController.verDetalleProducto(codigoProducto);
        });

        // Verificamos que se llamó al servicio con el código esperado
        verify(gestorBodegasService, times(1)).verDetalleProducto(codigoProducto);
    }
}



