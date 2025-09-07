package co.edu.uniquindio.dto.objects.producto;

import jakarta.validation.constraints.NotBlank;

public record RegistrarProductoExistenteDto(

        @NotBlank String codigoProducto,
        @NotBlank Integer cantidad,

        // Parte del movimiento
        @NotBlank String emaiPersonalBodega,
        String descripcion

) {
}
