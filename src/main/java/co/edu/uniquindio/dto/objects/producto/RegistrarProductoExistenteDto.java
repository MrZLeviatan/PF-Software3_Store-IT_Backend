package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.TipoProducto;
import jakarta.validation.constraints.NotBlank;


public record RegistrarProductoExistenteDto(

        @NotBlank String codigoProducto,
        @NotBlank Integer cantidad,
        TipoProducto tipoProducto,

        // Parte del movimiento
        @NotBlank String emaiPersonalBodega,
        String descripcion

) {
}
