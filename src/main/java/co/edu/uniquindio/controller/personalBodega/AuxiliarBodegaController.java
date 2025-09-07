package co.edu.uniquindio.controller.personalBodega;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auxiliar-bodega")
@RequiredArgsConstructor
public class AuxiliarBodegaController {

    private final AuxiliarBodegaService auxiliarBodegaService;

    // ️Registrar un nuevo producto
    @PostMapping("/productos")
    public ResponseEntity<MensajeDto<String>> registrarNuevoProducto(
            @ModelAttribute @Valid RegistroNuevoProductoDto registroNuevoProductoDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoEncontradoException {

        auxiliarBodegaService.RegistroNuevoProducto(registroNuevoProductoDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false, "Producto registrado correctamente. Pendiente de autorización"));
    }


    // 2Agregar cantidad a producto existente
    @PostMapping("/productos/stock")
    public ResponseEntity<MensajeDto<String>> agregarCantidadProducto(@RequestBody @Valid RegistrarProductoExistenteDto registrarProductoExistenteDto)
            throws ElementoNulosException, ElementoNoEncontradoException {

        auxiliarBodegaService.AgregarCantidadProducto(registrarProductoExistenteDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false, "Cantidad agregada correctamente. Pendiente de autorización."));
    }


    // Retirar producto
    @PostMapping("/productos/retiro")
    public ResponseEntity<MensajeDto<String>> retiroProducto(@RequestBody @Valid RetiroProductoDto retiroProductoDto) throws ElementoNoValidoException, ElementoNoEncontradoException {

        auxiliarBodegaService.RetiroProducto(retiroProductoDto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Retiro registrado correctamente."));
    }


    // Ver detalle de un producto
    @GetMapping("/productos/{codigoProducto}")
    public ResponseEntity<MensajeDto<ProductoDto>> verDetalleProducto(@PathVariable String codigoProducto) throws ElementoNoEncontradoException {

            ProductoDto productoDto = auxiliarBodegaService.verDetalleProducto(codigoProducto);
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
        List<ProductoDto> productos = auxiliarBodegaService.listarProductos(codigoProducto, tipoProducto, estadoProducto, idBodega, pagina, size);
        return ResponseEntity.ok().body(new MensajeDto<>(false , productos));
    }
}

