package co.edu.uniquindio.service.objects.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.MovimientoMapper;
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

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {


    private final ProductoRepo productoRepo;
    private final ProductoMapper productoMapper;
    private final MovimientoMapper movimientoMapper;

    @Override
    public ProductoDto verDetalleProducto(String codigoProducto)
            throws ElementoNoEncontradoException {

        return productoMapper.toDto(obtenerProducto(codigoProducto));
    }

    @Override
    public Producto obtenerProducto(String codigoProducto) throws ElementoNoEncontradoException {
        return productoRepo.findByCodigoProducto(codigoProducto)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PRODUCTO_NO_EXISTE));
    }


    @Override
    public Producto obtenerProductoAutorizado(String codigoProducto) throws ElementoNoEncontradoException {
        return productoRepo.findByCodigoProductoAndIsAutorizadoTrue(codigoProducto)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PRODUCTO_NO_EXISTE));
    }


    @Override
    public List<MovimientosProductoDto> obtenerMovimientosProductoEspecifico(String codigoProducto) throws ElementoNoEncontradoException {

        Producto producto = obtenerProducto(codigoProducto);

        return producto.getHistorialMovimientos().stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductoDto> listarProductos(String codigoProducto, TipoProducto tipoProducto,
                                             EstadoProducto estadoProducto, String idBodega, int pagina, int size) {

        // 1. Construir Pageable para paginación
        Pageable pageable = PageRequest.of(pagina, size);

        // 2. Crear predicado dinámico con filtros opcionales
        Specification<Producto> spec = Specification.where(null);

        // 3. Filtrar por código de producto si se proporciona
        if (codigoProducto != null && !codigoProducto.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("codigoProducto"), codigoProducto));
        }

        // 4. Filtrar por tipo de producto si se proporciona
        if (tipoProducto != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("tipoProducto"), tipoProducto));
        }

        // 5. Filtrar por estado del producto si se proporciona
        if (estadoProducto != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("estadoProducto"), estadoProducto));
        }

        // 6. Filtrar por bodega si se proporciona
        if (idBodega != null && !idBodega.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("bodega").get("id"), Long.parseLong(idBodega)));
        }

        // 7. Obtener la lista paginada de productos filtrados
        Page<Producto> productosPage = productoRepo.findAll(spec, pageable);

        // 8. Convertir a DTOs y devolver
        return productosPage.getContent().stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductoDto> listarProductosAutorizados(String codigoProducto, TipoProducto tipoProducto,
                                             EstadoProducto estadoProducto, String idBodega, int pagina, int size) {

        // 1. Construir Pageable para paginación / Build Pageable for pagination
        Pageable pageable = PageRequest.of(pagina, size);

        // 2. Crear predicado dinámico con filtros opcionales / Create dynamic predicate with optional filters
        Specification<Producto> spec = Specification.where(
                // 🔹 Filtro obligatorio: solo productos autorizados / Mandatory filter: only authorized products
                (root, query, builder) -> builder.isTrue(root.get("isAutorizado"))
        );

        // 3. Filtrar por código de producto si se proporciona / Filter by product code if provided
        if (codigoProducto != null && !codigoProducto.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("codigoProducto"), codigoProducto));
        }

        // 4. Filtrar por tipo de producto si se proporciona / Filter by product type if provided
        if (tipoProducto != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("tipoProducto"), tipoProducto));
        }

        // 5. Filtrar por estado del producto si se proporciona / Filter by product state if provided
        if (estadoProducto != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("estadoProducto"), estadoProducto));
        }

        // 6. Filtrar por bodega si se proporciona / Filter by warehouse if provided
        if (idBodega != null && !idBodega.isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("bodega").get("id"), Long.parseLong(idBodega)));
        }

        // 7. Obtener la lista paginada de productos filtrados / Get paginated list of filtered products
        Page<Producto> productosPage = productoRepo.findAll(spec, pageable);

        // 8. Convertir a DTOs y devolver / Convert to DTOs and return
        return productosPage.getContent().stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
    }



}
