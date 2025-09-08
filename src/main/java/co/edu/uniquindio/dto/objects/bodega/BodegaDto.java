package co.edu.uniquindio.dto.objects.bodega;

import co.edu.uniquindio.dto.common.UbicacionDto;

public record BodegaDto(


        Long id,
        UbicacionDto ubicacion,
        String direccion,
        String telefono

) {
}
