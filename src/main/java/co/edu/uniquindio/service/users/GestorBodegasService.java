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

public interface GestorBodegasService {


    void autorizarMovimiento(AutorizacionProductoDto autorizacionProductoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoIncorrectoException;

    ProductoDto verDetalleProducto(String codigoProducto) throws ElementoNoEncontradoException;

    List<ProductoDto> listarProductos (
            String codigoProducto, TipoProducto tipoProducto,
            EstadoProducto estadoProducto, String idBodega, int pagina, int size);

    List<MovimientosProductoDto> obtenerMovimientoProductoEspecifico(String codigoProducto) throws ElementoNoEncontradoException;

    MovimientosProductoDto verDetallesMovimiento(Long idMovimientosProducto) throws ElementoNoEncontradoException;

    List<MovimientosProductoDto> obtenerMovimientosProducto(String codigoProducto, TipoMovimiento tipoMovimiento,
                                                            LocalDateTime fechaMovimiento, String emailPersonalResponsable,
                                                            String emailPersonalAutorizado, String idBodega, int pagina, int size);

}
