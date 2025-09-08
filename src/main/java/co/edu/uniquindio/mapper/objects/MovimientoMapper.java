package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {


    @Mapping(target = "idProducto", source = "producto.id")
    @Mapping(target = "emailPersonalResponsable", source = "personalResponsable.user.email")
    @Mapping(target = "emailPersonalAutorizado", source = "personalAutorizacion.user.email")
    @Mapping(target = "isVerificado", source = "verificado")
    MovimientosProductoDto toDto (MovimientosProducto movimientosProducto);

}
