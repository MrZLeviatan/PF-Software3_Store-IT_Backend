package co.edu.uniquindio.controller.personalBodega;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.dto.objects.producto.AutorizacionProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoCoincideException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import co.edu.uniquindio.model.enums.TipoProducto;
import co.edu.uniquindio.service.users.GestorBodegasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/** Este es un controlador REST para la gestión de bodegas.
  Proporciona endpoints para que los clientes interactúen con las funcionalidades
  relacionadas con la administración de bodegas, como el registro y la búsqueda de productos.
 */
@RestController
@RequestMapping("/api/gestor-bodega")
@RequiredArgsConstructor
public class GestorBodegaController {

    private final GestorBodegasService gestorBodegasService;

    //  ️Autorizar movimiento de producto
    @PostMapping("/movimientos/autorizar")
    public ResponseEntity<MensajeDto<String>> autorizarMovimiento(@RequestBody @Valid AutorizacionProductoDto autorizacionProductoDto)
            throws ElementoIncorrectoException, ElementoNoCoincideException, ElementoNoEncontradoException {
            gestorBodegasService.autorizarMovimiento(autorizacionProductoDto);

            return ResponseEntity.ok().body(new MensajeDto<>(false, "Movimiento verificado correctamente."));
    }

    // Ver detalle de un producto
    @GetMapping("/productos/{codigoProducto}")
    public ResponseEntity<MensajeDto<ProductoDto>> verDetalleProducto(@PathVariable String codigoProducto)
            throws ElementoNoEncontradoException {

        ProductoDto productoDto = gestorBodegasService.verDetalleProducto(codigoProducto);
        return ResponseEntity.ok().body(new MensajeDto<>(false , productoDto));
    }

    // Listar productos con filtros y paginación
    @GetMapping("/productos")
    public ResponseEntity<MensajeDto<List<ProductoDto>>> listarProductos(
            @RequestParam(required = false) String codigoProducto,
            @RequestParam(required = false) TipoProducto tipoProducto,
            @RequestParam(required = false) EstadoProducto estadoProducto,
            @RequestParam(required = false) String idBodega,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<ProductoDto> productos = gestorBodegasService.listarProductos(codigoProducto, tipoProducto, estadoProducto, idBodega, pagina, size);
        return ResponseEntity.ok().body(new MensajeDto<>(false, productos));
    }


    // Ver detalle de un movimiento de producto
    @GetMapping("/movimientos/{idMovimiento}")
    public ResponseEntity<MensajeDto<MovimientosProductoDto>> verDetallesMovimiento(@PathVariable Long idMovimiento)
            throws ElementoNoEncontradoException {

        MovimientosProductoDto movimientoDto = gestorBodegasService.verDetallesMovimiento(idMovimiento);
        return ResponseEntity.ok().body(new MensajeDto<>(false , movimientoDto));
    }


    // Obtener lista de movimientos de un producto específico por su código
    @GetMapping("/movimientos/producto/{codigoProducto}")
    public ResponseEntity<MensajeDto<List<MovimientosProductoDto>>> obtenerMovimientosProductoEspecifico(
            @PathVariable String codigoProducto
    ) throws ElementoNoEncontradoException {

        // 1. Llamar al servicio para obtener los movimientos del producto
        List<MovimientosProductoDto> movimientos = gestorBodegasService.obtenerMovimientoProductoEspecifico(codigoProducto);

        // 2. Retornar la respuesta en un contenedor estándar
        return ResponseEntity.ok().body(new MensajeDto<>(false, movimientos));
    }


    // Obtener lista de movimientos de productos con filtros y paginación
    @GetMapping("/movimientos")
    public ResponseEntity<MensajeDto<List<MovimientosProductoDto>>> obtenerMovimientosProducto(
            @RequestParam(required = false) String codigoProducto,
            @RequestParam(required = false) TipoMovimiento tipoMovimiento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaMovimiento,
            @RequestParam(required = false) String emailPersonalResponsable,
            @RequestParam(required = false) String emailPersonalAutorizado,
            @RequestParam(required = false) String idBodega,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<MovimientosProductoDto> movimientos = gestorBodegasService.obtenerMovimientosProducto(
                codigoProducto, tipoMovimiento, fechaMovimiento, emailPersonalResponsable, emailPersonalAutorizado, idBodega, pagina, size
        );
        return ResponseEntity.ok().body(new MensajeDto<>(false , movimientos));
    }
}
