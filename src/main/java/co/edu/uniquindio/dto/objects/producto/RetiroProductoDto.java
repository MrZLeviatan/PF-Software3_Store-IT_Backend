package co.edu.uniquindio.dto.objects.producto;

import jakarta.validation.constraints.NotBlank;
/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar la información
  necesaria para el retiro de un producto de una bodega.
 *
  Utiliza anotaciones de validación de Jakarta para garantizar que los datos proporcionados
  sean válidos antes de que se procese la solicitud. Esto incluye el código del producto,
  la cantidad a retirar y los detalles del personal responsable.
 */
public record RetiroProductoDto(
        // Código único del producto que se va a retirar. No puede estar en blanco.
        @NotBlank String codigoProducto,
        // Cantidad del producto que se retira.
        Integer cantidad,
        // Email del personal de bodega responsable del retiro. No puede estar en blanco.
        @NotBlank String emailPersonalResponsable,
        // Descripción opcional del motivo del retiro.
        String descripcion
) {
}
