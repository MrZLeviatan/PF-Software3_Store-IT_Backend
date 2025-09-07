package co.edu.uniquindio.dto.objects.producto;

import jakarta.validation.constraints.NotBlank;


public record RetiroProductoDto(


        @NotBlank String codigoProducto,
        Integer cantidad,

        // Parte del movimiento
        @NotBlank String emailPersonalResponsable,
        String descripcion

) {
}
