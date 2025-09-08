package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record RegistrarProductoExistenteDto(

        @NotBlank String codigoProducto,
        @NotNull Integer cantidad,
        TipoProducto tipoProducto,

        // Parte del movimiento
        @NotBlank String emailPersonalBodega,
        String descripcion

) {
}
