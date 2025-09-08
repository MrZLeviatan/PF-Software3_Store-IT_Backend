package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.producto.RegistroNuevoProductoDto;
import co.edu.uniquindio.model.entities.objects.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Este es un Mapper de MapStruct para la entidad Producto.
  Su función es convertir objetos DTO a entidades y viceversa.
  Se utiliza para mapear datos desde un DTO de registro de nuevo producto a una entidad Producto,
  y también para convertir una entidad Producto a un DTO para su visualización.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper {

    // Convierte un DTO (RegistroNuevoProductoDto) a una entidad (Producto).
    // Ignora los campos 'id' e 'imagen', establece 'estadoProducto' en 'EN_BODEGA' y
    // crea una nueva lista vacía para 'historialMovimientos'.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    @Mapping(target = "estadoProducto", constant = "EN_BODEGA")
    @Mapping(target = "bodega", ignore = true)
    @Mapping(target = "historialMovimientos", expression = "java(new java.util.ArrayList<>())")
    Producto toEntityNew(RegistroNuevoProductoDto registroNuevoProductoDto);

    // Convierte una entidad (Producto) a un DTO (ProductoDto).
    // Mapea el ID de la bodega del objeto 'bodega' al campo 'idBodega' del DTO.
    @Mapping(target = "idBodega", source = "bodega.id")
    ProductoDto toDto(Producto producto);
}