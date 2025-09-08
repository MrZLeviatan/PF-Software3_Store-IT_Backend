package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
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

import java.util.List;

public interface AuxiliarBodegaService {


    void RegistroNuevoProducto(RegistroNuevoProductoDto registroNuevoProductoDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoEncontradoException, ElementoNoValidoException;

    void AgregarCantidadProducto(RegistrarProductoExistenteDto registrarProductoExistenteDto)
            throws ElementoNoEncontradoException, ElementoNulosException;

    void RetiroProducto(RetiroProductoDto retiroProductoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException;

    ProductoDto verDetalleProducto(String codigoProducto)
            throws ElementoNoEncontradoException;

    List<ProductoDto> listarProductos(String codigoProducto, TipoProducto tipoProducto,
                                      EstadoProducto estadoProducto, String idBodega, int pagina, int size);


    List<BodegaDto> obtenerBodegas();

}
