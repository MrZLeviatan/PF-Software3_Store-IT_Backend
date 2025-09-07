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

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {


    private final MovimientoMapper movimientoMapper;
    private final MovimientoProductoRepo movimientoProductoRepo;



    @Override
    public MovimientosProducto registrarMovimientoProducto(String descripcion,
                                                           PersonalBodega personalResponsable, TipoMovimiento tipoMovimiento,
                                                           Producto producto, Integer cantidad) {

        MovimientosProducto movimientosProducto = new MovimientosProducto();
        movimientosProducto.setDescripcion(descripcion);
        movimientosProducto.setTipoMovimiento(tipoMovimiento);
        movimientosProducto.setFechaMovimiento(LocalDateTime.now());
        movimientosProducto.setProducto(producto);
        movimientosProducto.setCantidad(cantidad);
        movimientosProducto.setPersonalResponsable(personalResponsable);

        movimientoProductoRepo.save(movimientosProducto);
        return movimientosProducto;
    }


    @Override
    public MovimientosProductoDto verDetalleMovimiento(Long idMovimiento)
            throws ElementoNoEncontradoException {
        return movimientoMapper.toDto(obtenerMovimiento(idMovimiento));
    }


    @Override
    public MovimientosProducto obtenerMovimiento(Long idMovimiento) throws ElementoNoEncontradoException {
        return   movimientoProductoRepo.findById(idMovimiento)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.MOVIMIENTO_NO_EXISTE));
    }

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

        // 3. Filtrar por código de producto si se proporciona
        if (codigoProducto != null && !codigoProducto.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("producto").get("codigoProducto"), codigoProducto));
        }

        // 4. Filtrar por tipo de movimiento si se proporciona
        if (tipoMovimiento != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("tipoMovimiento"), tipoMovimiento));
        }

        // 5. Filtrar por fecha de movimiento si se proporciona
        if (fechaMovimiento != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("fechaMovimiento"), fechaMovimiento));
        }

        // 6. Filtrar por email del personal responsable si se proporciona
        if (emailPersonalResponsable != null && !emailPersonalResponsable.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("personalResponsable").get("email"), emailPersonalResponsable));
        }

        // 7. Filtrar por email del personal autorizado si se proporciona
        if (emailPersonalAutorizado != null && !emailPersonalAutorizado.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("personalAutorizacion").get("email"), emailPersonalAutorizado));
        }

        // 8. Filtrar por bodega si se proporciona
        if (idBodega != null && !idBodega.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("producto").get("bodega").get("id"), Long.parseLong(idBodega)));
        }

        // 9. Obtener la lista paginada de movimientos filtrados
        Page<MovimientosProducto> movimientosPage = movimientoProductoRepo.findAll(spec, pageable);

        // 10. Convertir a DTOs y devolver
        return movimientosPage.getContent().stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }

}
