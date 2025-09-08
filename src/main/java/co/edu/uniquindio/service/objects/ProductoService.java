package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;

import java.util.List;

/** Servicio encargado de la gestión de productos en Store-IT.
   Define las operaciones principales relacionadas con la consulta
   de detalles, obtención y listado de productos dentro de las bodegas.
 */
public interface ProductoService {

    // Consulta los detalles de un producto específico a partir de su código
    ProductoDto verDetalleProducto(String codigoProducto) throws ElementoNoEncontradoException;

    // Obtiene un producto sin validar su estado o autorización
    Producto obtenerProducto(String codigoProducto) throws ElementoNoEncontradoException;

    // Obtiene un producto validando que esté autorizado para movimientos
    Producto obtenerProductoAutorizado(String codigoProducto) throws ElementoNoEncontradoException;

    /** Lista los productos aplicando filtros como código, tipo, estado
       y bodega. Incluye soporte de paginación.
     */
    List<ProductoDto> listarProductos(
            String codigoProducto, TipoProducto tipoProducto,
            EstadoProducto estadoProducto, String idBodega, int pagina, int size);
}
