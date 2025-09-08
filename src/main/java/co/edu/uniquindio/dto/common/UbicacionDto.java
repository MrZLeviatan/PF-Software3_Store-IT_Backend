package co.edu.uniquindio.dto.common;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para representar
  la información de una ubicación geográfica.
 *
  Su propósito es encapsular los datos de un país, una ciudad, y las coordenadas de
  latitud y longitud. Las anotaciones de validación garantizan que los datos sean
  correctos, especialmente para las coordenadas, que deben estar dentro de los
  rangos geográficos válidos.
 */
public record UbicacionDto(

        // El país de la ubicación. No puede estar en blanco.
        @NotBlank String pais,
        // La ciudad de la ubicación. No puede estar en blanco.
        @NotBlank String ciudad,
        // La latitud de la ubicación. No puede ser nula y debe estar entre -90.0 y 90.0.
        @NotNull
        @DecimalMin(value = "-90.0", inclusive = true, message = "Latitud inválida.")
        @DecimalMax(value = "90.0", inclusive = true, message = "Latitud inválida.")
        Double latitud,

        // La longitud de la ubicación. No puede ser nula y debe estar entre -180.0 y 180.0.
        @NotNull
        @DecimalMin(value = "-180.0", inclusive = true, message = "Longitud inválida.")
        @DecimalMax(value = "180.0", inclusive = true, message = "Longitud inválida.")
        Double longitud
) {
}