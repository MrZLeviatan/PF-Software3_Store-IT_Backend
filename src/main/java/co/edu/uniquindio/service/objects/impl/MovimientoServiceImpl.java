package co.edu.uniquindio.service.objects.impl;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.mapper.objects.MovimientoMapper;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import co.edu.uniquindio.repository.objects.MovimientoProductoRepo;
import co.edu.uniquindio.service.objects.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {


    private MovimientoMapper movimientoMapper;
    private MovimientoProductoRepo movimientoProductoRepo;



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
    public MovimientosProductoDto verDetalleMovimiento(Long idMovimiento) {
        return null;
    }

    @Override
    public List<MovimientosProductoDto> listarMovimientos(String codigoProducto, TipoMovimiento tipoMovimiento, LocalDateTime fechaMovimiento, String emailPersonalResponsable, String emailPersonalAutorizado, String idBodega) {
        return List.of();
    }
}
