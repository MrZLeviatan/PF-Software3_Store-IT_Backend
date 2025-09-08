package co.edu.uniquindio.dto.objects.producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgregarCantidadDto(

        @NotBlank(message = "El código del producto es obligatorio")
        String codigoProducto,

        @NotNull(message = "La cantidad no puede ser nula")
        @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
        Integer cantidad
) {}
