package co.edu.uniquindio.dto.objects.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record RetiroProductoDto(


        @NotBlank String codigoProducto,
        @NotNull Integer cantidad,

        // Parte del movimiento
        @NotBlank String emailPersonalResponsable,
        String descripcion

) {
}
