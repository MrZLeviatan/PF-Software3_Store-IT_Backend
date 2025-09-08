package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Este es un Mapper de MapStruct para la entidad MovimientosProducto.
  Su objetivo es convertir objetos de la entidad MovimientosProducto a objetos DTO (Data Transfer Object)
  de MovimientosProductoDto.
 */
@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    // Convierte un objeto MovimientosProducto en un objeto MovimientosProductoDto.
    // Los @Mapping se usan para especificar cómo mapear los campos,
    // en este caso, se extraen los IDs de los objetos relacionados.
    @Mapping(target = "idProducto", source = "producto.id")
    @Mapping(target = "idPersonalResponsable", source = "personalResponsable.id")
    @Mapping(target = "idPersonalAutorizado", source = "personalAutorizacion.id")
    MovimientosProductoDto toDto (MovimientosProducto movimientosProducto);
}
