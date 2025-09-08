package co.edu.uniquindio.mapper.objects;

import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.model.entities.objects.Bodega;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BodegaMapper {


    BodegaDto toDto(Bodega bodega);

}
