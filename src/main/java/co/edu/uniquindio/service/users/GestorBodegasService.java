package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.dto.objects.producto.AutorizacionProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoCoincideException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import co.edu.uniquindio.model.enums.TipoProducto;

import java.time.LocalDateTime;
import java.util.List;

/** Interfaz del servicio encargado de gestionar las operaciones relacionadas con la administración
** de bodegas en Store-IT. Define las funcionalidades principales para la autorización de movimientos,
** consulta de productos y registro de entradas y salidas en el sistema.
 */
public interface GestorBodegasService {

    /** Autoriza un movimiento de producto (entrada/salida) validando la información y las reglas de negocio.
    // Puede lanzar excepciones en caso de que:
    // - No se encuentre el producto.
    // - El dato no coincida con el responsable/autorizado.
    // - Algún elemento esté en estado incorrecto.
     */
    void autorizarMovimiento(AutorizacionProductoDto autorizacionProductoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoIncorrectoException;

    // Consulta los detalles de un producto específico a partir de su código.
    ProductoDto verDetalleProducto(String codigoProducto) throws ElementoNoEncontradoException;

    // Lista productos aplicando filtros como código, tipo, estado, bodega y con soporte de paginación.
    List<ProductoDto> listarProductos (
            String codigoProducto, TipoProducto tipoProducto,
            EstadoProducto estadoProducto, String idBodega, int pagina, int size);

    List<MovimientosProductoDto> obtenerMovimientoProductoEspecifico(String codigoProducto) throws ElementoNoEncontradoException;

    // Obtiene la información detallada de un movimiento específico por su ID.
    MovimientosProductoDto verDetallesMovimiento(Long idMovimientosProducto) throws ElementoNoEncontradoException;

    // Recupera el historial de movimientos (entradas/salidas) de un producto,
    // filtrando por tipo de movimiento, fechas, responsables, autorizados y bodega.
    // También soporta paginación.
    List<MovimientosProductoDto> obtenerMovimientosProducto(String codigoProducto, TipoMovimiento tipoMovimiento,
                                                            LocalDateTime fechaMovimiento, String emailPersonalResponsable,
                                                            String emailPersonalAutorizado, String idBodega, int pagina, int size);

}
