package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar
  la información necesaria para actualizar la contraseña de un usuario.
 *
  Su propósito es transferir de manera segura el email del usuario y la nueva contraseña,
  garantizando que los datos sean válidos mediante anotaciones de validación.
  Esto ayuda a asegurar que la contraseña no sea nula, no esté en blanco y cumpla con
  una longitud mínima de 8 caracteres.
 */
public record ActualizarPasswordDto (
        // El email del usuario cuya contraseña será actualizada. No puede ser nulo ni estar en blanco.
        @NotBlank @NotNull String email,
        // La nueva contraseña. No puede estar en blanco y debe tener un mínimo de 8 caracteres.
        @NotBlank @Size(min = 8) String nuevaPassword
){
}