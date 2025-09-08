package co.edu.uniquindio.service.objects.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.MovimientoMapper;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import co.edu.uniquindio.repository.objects.MovimientoProductoRepo;
import co.edu.uniquindio.service.objects.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** Implementación del servicio de movimientos en Store-IT.
   Se encarga de registrar, consultar y listar los movimientos de productos
   realizados en las bodegas (ingresos, retiros, autorizaciones, etc.).
*/
@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    // Mapper para convertir entre entidades y DTOs de movimientos
    private final MovimientoMapper movimientoMapper;

    // Repositorio para persistir y consultar movimientos de productos
    private final MovimientoProductoRepo movimientoProductoRepo;

    // Registra un nuevo movimiento de producto en la base de datos. */
    @Override
    public MovimientosProducto registrarMovimientoProducto(String descripcion,
                                                           PersonalBodega personalResponsable,
                                                           TipoMovimiento tipoMovimiento,
                                                           Producto producto,
                                                           Integer cantidad) {

        // Crear objeto de movimiento con la información proporcionada
        MovimientosProducto movimientosProducto = new MovimientosProducto();
        movimientosProducto.setDescripcion(descripcion);
        movimientosProducto.setTipoMovimiento(tipoMovimiento);
        movimientosProducto.setFechaMovimiento(LocalDateTime.now());
        movimientosProducto.setProducto(producto);
        movimientosProducto.setCantidad(cantidad);
        movimientosProducto.setPersonalResponsable(personalResponsable);

        // Guardar movimiento en la base
        movimientoProductoRepo.save(movimientosProducto);
        return movimientosProducto;
    }

    // Devuelve el detalle de un movimiento específico a partir de su ID.
    @Override
    public MovimientosProductoDto verDetalleMovimiento(Long idMovimiento)
            throws ElementoNoEncontradoException {
        return movimientoMapper.toDto(obtenerMovimiento(idMovimiento));
    }

    // Obtiene un movimiento en formato entidad a partir de su ID.
    @Override
    public MovimientosProducto obtenerMovimiento(Long idMovimiento)
            throws ElementoNoEncontradoException {
        return movimientoProductoRepo.findById(idMovimiento)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.MOVIMIENTO_NO_EXISTE));
    }

    /** Lista los movimientos registrados aplicando filtros opcionales como
       producto, tipo de movimiento, fecha, responsables, autorizados y bodega.
    */
    @Override
    public List<MovimientosProductoDto> listarMovimientos(
            String codigoProducto,
            TipoMovimiento tipoMovimiento,
            LocalDateTime fechaMovimiento,
            String emailPersonalResponsable,
            String emailPersonalAutorizado,
            String idBodega,
            int pagina,
            int size) {

        // 1. Construir Pageable para paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // 2. Crear predicado dinámico con filtros opcionales
        Specification<MovimientosProducto> spec = Specification.where(null);

        // 3. Filtrar por código de producto
        if (codigoProducto != null && !codigoProducto.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("producto").get("codigoProducto"), codigoProducto));
        }

        // 4. Filtrar por tipo de movimiento
        if (tipoMovimiento != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("tipoMovimiento"), tipoMovimiento));
        }

        // 5. Filtrar por fecha de movimiento
        if (fechaMovimiento != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("fechaMovimiento"), fechaMovimiento));
        }

        // 6. Filtrar por email del personal responsable
        if (emailPersonalResponsable != null && !emailPersonalResponsable.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("personalResponsable").get("email"), emailPersonalResponsable));
        }

        // 7. Filtrar por email del personal autorizado
        if (emailPersonalAutorizado != null && !emailPersonalAutorizado.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("personalAutorizacion").get("email"), emailPersonalAutorizado));
        }

        // 8. Filtrar por bodega
        if (idBodega != null && !idBodega.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("producto").get("bodega").get("id"), Long.parseLong(idBodega)));
        }

        // 9. Obtener la lista paginada de movimientos
        Page<MovimientosProducto> movimientosPage = movimientoProductoRepo.findAll(spec, pageable);

        // 10. Convertir a DTOs y devolver
        return movimientosPage.getContent().stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }
}
