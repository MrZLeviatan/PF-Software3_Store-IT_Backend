package co.edu.uniquindio.service.objects.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.ProductoMapper;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;
import co.edu.uniquindio.repository.objects.ProductoRepo;
import co.edu.uniquindio.service.objects.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Implementación del servicio de productos en Store-IT.
   Se encarga de gestionar la consulta de productos, incluyendo la obtención de
   detalles, búsqueda con filtros dinámicos y verificación de autorización.
 */
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    // Dependencia al repositorio de productos para la persistencia
    private final ProductoRepo productoRepo;

    // Mapper para convertir entidades Producto a DTOs
    private final ProductoMapper productoMapper;

    /* Obtiene los detalles de un producto específico y lo devuelve en formato DTO.
       Lanza excepción si el producto no existe. */
    @Override
    public ProductoDto verDetalleProducto(String codigoProducto)
            throws ElementoNoEncontradoException {
        return productoMapper.toDto(obtenerProducto(codigoProducto));
    }

    /** Recupera un producto directamente desde la base de datos por su código.
       Lanza excepción si no se encuentra.
     */
    @Override
    public Producto obtenerProducto(String codigoProducto) throws ElementoNoEncontradoException {
        return productoRepo.findByCodigoProducto(codigoProducto)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PRODUCTO_NO_EXISTE));
    }

    /** Recupera un producto autorizado (activo para operaciones) desde la base de datos.
       Lanza excepción si no existe o no está autorizado.
     */
    @Override
    public Producto obtenerProductoAutorizado(String codigoProducto) throws ElementoNoEncontradoException {
        return productoRepo.findByCodigoProductoAndIsAutorizadoTrue(codigoProducto)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PRODUCTO_NO_EXISTE));
    }

    /** Lista productos aplicando filtros opcionales como código, tipo, estado y bodega.
       Los resultados se devuelven de manera paginada y transformados a DTOs.
     */
    @Override
    public List<ProductoDto> listarProductos(String codigoProducto, TipoProducto tipoProducto,
                                             EstadoProducto estadoProducto, String idBodega,
                                             int pagina, int size) {

        // 1. Configurar paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // 2. Crear especificación inicial vacía
        Specification<Producto> spec = Specification.where(null);

        // 3. Filtrar por código de producto
        if (codigoProducto != null && !codigoProducto.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("codigoProducto"), codigoProducto));
        }

        // 4. Filtrar por tipo de producto
        if (tipoProducto != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("tipoProducto"), tipoProducto));
        }

        // 5. Filtrar por estado de producto
        if (estadoProducto != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("estadoProducto"), estadoProducto));
        }

        // 6. Filtrar por ID de bodega
        if (idBodega != null && !idBodega.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("bodega").get("id"), Long.parseLong(idBodega)));
        }

        // 7. Ejecutar consulta paginada con los filtros
        Page<Producto> productosPage = productoRepo.findAll(spec, pageable);

        // 8. Mapear entidades a DTOs y devolver lista
        return productosPage.getContent().stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }
}
