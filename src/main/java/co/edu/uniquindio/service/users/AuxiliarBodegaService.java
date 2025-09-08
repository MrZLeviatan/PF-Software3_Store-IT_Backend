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

/**
 * Interfaz del servicio para auxiliares de bodega en Store-IT.
 * Define las operaciones básicas relacionadas con la gestión de productos,
 * como registro, actualización de cantidades, retiros y consultas.
 */
public interface AuxiliarBodegaService {

    /** Registra un nuevo producto en la bodega.
    // Puede fallar si:
    // - Ya existe un producto con el mismo código.
    // - Algún campo requerido está nulo.
    // - No se encuentra la bodega asociada.
     */
    void RegistroNuevoProducto(RegistroNuevoProductoDto registroNuevoProductoDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoEncontradoException, ElementoNoValidoException;

    // Agrega cantidad a un producto ya existente en la bodega.
    // Puede lanzar excepciones si el producto no existe o hay datos nulos.
    void AgregarCantidadProducto(RegistrarProductoExistenteDto registrarProductoExistenteDto)
            throws ElementoNoEncontradoException, ElementoNulosException;

    // Registra el retiro (salida) de un producto de la bodega.
    // Puede fallar si el producto no existe o si la operación es inválida.
    void RetiroProducto(RetiroProductoDto retiroProductoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException;

    // Consulta los detalles de un producto específico según su código.
    ProductoDto verDetalleProducto(String codigoProducto)
            throws ElementoNoEncontradoException;

    // Lista productos aplicando filtros por código, tipo, estado y bodega.
    // Soporta paginación para manejar grandes cantidades de resultados.
    List<ProductoDto> listarProductos(String codigoProducto, TipoProducto tipoProducto,
                                      EstadoProducto estadoProducto, String idBodega, int pagina, int size);


    List<BodegaDto> obtenerBodegas();

}
