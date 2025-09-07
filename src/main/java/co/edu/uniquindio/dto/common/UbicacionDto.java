package co.edu.uniquindio.dto.common;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UbicacionDto(


        @NotBlank String pais,
        @NotBlank String ciudad,
        @NotNull
        @DecimalMin(value = "-90.0", inclusive = true, message = "Latitud inválida.")
        @DecimalMax(value = "90.0", inclusive = true, message = "Latitud inválida.")
        Double latitud,

        @NotNull
        @DecimalMin(value = "-180.0", inclusive = true, message = "Longitud inválida.")
        @DecimalMax(value = "180.0", inclusive = true, message = "Longitud inválida.")
        Double longitud
) {
}
