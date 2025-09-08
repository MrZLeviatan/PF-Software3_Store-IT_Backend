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
    @Mapping(target = "emailPersonalResponsable", source = "personalResponsable.user.email")
    @Mapping(target = "emailPersonalAutorizado", source = "personalAutorizacion.user.email")
    @Mapping(target = "isVerificado", source = "verificado")
    MovimientosProductoDto toDto (MovimientosProducto movimientosProducto);
}
