package co.edu.uniquindio.dto.common.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para la creación
  de un nuevo usuario.
 *
  Su propósito es encapsular las credenciales básicas necesarias para un registro,
  como el email y la contraseña. Las anotaciones de validación garantizan que el
  email tenga un formato válido y que la contraseña cumpla con una longitud mínima
  de 8 caracteres.
 */

public record CrearUserDto(

        // El email del nuevo usuario. No puede estar en blanco y debe tener un formato de email válido.
        @Email @NotBlank String email,
        // La contraseña del nuevo usuario. No puede estar en blanco y debe tener un mínimo de 8 caracteres.
        @NotBlank @Size(min = 8) String password
) {
}
