package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

/** Servicio encargado de la gestión de movimientos de productos en Store-IT.
   Permite registrar nuevos movimientos, consultar detalles de un movimiento
   específico y listar los movimientos realizados en las bodegas.
 */
public interface MovimientoService {

    // Registra un nuevo movimiento de producto en el sistema
    MovimientosProducto registrarMovimientoProducto(String descripcion,
                                                    PersonalBodega personalResponsable,
                                                    TipoMovimiento tipoMovimiento,
                                                    Producto producto,
                                                    Integer cantidad);

    // Consulta los detalles de un movimiento específico por su ID
    MovimientosProductoDto verDetalleMovimiento(Long idMovimiento)
            throws ElementoNoEncontradoException;

    // Obtiene un movimiento directamente desde la entidad por su ID
    MovimientosProducto obtenerMovimiento(Long idMovimiento)
            throws ElementoNoEncontradoException;

    // Lista los movimientos aplicando filtros como producto, tipo, fecha, responsable y bodega
    List<MovimientosProductoDto> listarMovimientos(String codigoProducto,
                                                   TipoMovimiento tipoMovimiento,
                                                   LocalDateTime fechaMovimiento,
                                                   String emailPersonalResponsable,
                                                   String emailPersonalAutorizado,
                                                   String idBodega,
                                                   int pagina,
                                                   int size);
}
