package co.edu.uniquindio.dto.objects.movimientoProducto;

import co.edu.uniquindio.model.enums.TipoMovimiento;

import java.time.LocalDateTime;

public record MovimientosProductoDto(


        Long id,
        String descripcion,
        TipoMovimiento tipoMovimiento,
        LocalDateTime fechaMovimiento,
        LocalDateTime fechaAutorizacion,
        String descripcionAutorizado,
        String idProducto,
        String idPersonalResponsable,
        String idPersonalAutorizado

        ) {
}
