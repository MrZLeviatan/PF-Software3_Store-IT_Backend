package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;

import java.util.List;

public interface ProductoService {


    ProductoDto verDetalleProducto(String codigoProducto) throws ElementoNoEncontradoException;


    Producto obtenerProducto(String codigoProducto) throws ElementoNoEncontradoException;

    Producto obtenerProductoAutorizado(String codigoProducto) throws ElementoNoEncontradoException;

    List<MovimientosProductoDto> obtenerMovimientosProductoEspecifico(String codigoProducto) throws ElementoNoEncontradoException;

    List<ProductoDto> listarProductos (
            String codigoProducto, TipoProducto tipoProducto,
            EstadoProducto estadoProducto, String idBodega, int pagina, int size);

    List<ProductoDto> listarProductosAutorizados (
            String codigoProducto, TipoProducto tipoProducto,
            EstadoProducto estadoProducto, String idBodega, int pagina, int size);

}
