package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar
  las credenciales de un usuario al iniciar sesión.
 *
  Su propósito es encapsular el email y la contraseña, garantizando que ambos campos
  no estén vacíos y que el email tenga un formato válido mediante anotaciones de validación.
  Este DTO es esencial para la capa de seguridad, ya que estandariza la información
  de entrada para el proceso de autenticación.
 */
public record LoginDto(
        // El email del usuario. No puede estar en blanco y debe tener un formato de correo válido.
        @NotBlank @Email String email,
        // La contraseña del usuario. No puede estar en blanco.
        @NotBlank String password
) {
}