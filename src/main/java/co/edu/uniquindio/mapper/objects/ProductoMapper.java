package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.producto.RegistroNuevoProductoDto;
import co.edu.uniquindio.model.entities.objects.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    @Mapping(target = "estadoProducto", constant = "EN_BODEGA")
    @Mapping(target = "bodega", ignore = true)
    @Mapping(target = "historialMovimientos", expression = "java(new java.util.ArrayList<>())")
    Producto toEntityNew(RegistroNuevoProductoDto registroNuevoProductoDto);


    @Mapping(target = "idBodega", source = "bodega.id") // Obtenemos el ID de la bodega
    ProductoDto toDto(Producto producto);

}
