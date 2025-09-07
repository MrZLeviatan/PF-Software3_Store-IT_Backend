package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;

import java.time.LocalDateTime;

public record ProductoDto(

        Long id,
        String codigoProducto,
        String nombre,
        Integer cantidad,
        String imagen,
        String descripcion,
        TipoProducto tipoProducto,
        LocalDateTime fechaVencimiento,
        EstadoProducto estadoProducto,
        String idBodega

) {
}
