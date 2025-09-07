package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {


    MovimientosProducto registrarMovimientoProducto(String descripcion,
                                                    PersonalBodega personalResponsable, TipoMovimiento tipoMovimiento,
                                                    Producto producto, Integer cantidad);


    MovimientosProductoDto verDetalleMovimiento(Long idMovimiento)
            throws ElementoNoEncontradoException;


    MovimientosProducto obtenerMovimiento(Long idMovimiento)
            throws ElementoNoEncontradoException;


    List<MovimientosProductoDto> listarMovimientos (
            String codigoProducto, TipoMovimiento tipoMovimiento, LocalDateTime fechaMovimiento,
            String emailPersonalResponsable, String emailPersonalAutorizado, String idBodega, int pagina, int size);

}
