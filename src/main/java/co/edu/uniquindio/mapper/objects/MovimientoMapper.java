package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {


    @Mapping(target = "idProducto", source = "producto.id")
    @Mapping(target = "idPersonalResponsable", source = "personalResponsable.id")
    @Mapping(target = "idPersonalAutorizado", source = "personalAutorizacion.id")
    MovimientosProductoDto toDto (MovimientosProducto movimientosProducto);

}
