package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar la
  información necesaria para verificar un código de seguridad enviado a un email.
 *
  Su propósito es encapsular el correo electrónico del usuario y el código de verificación
  que ha ingresado. Las anotaciones de validación aseguran que el email no esté en blanco y
  tenga un formato válido, lo que es esencial para procesos como la recuperación de
  contraseña o la activación de cuentas.
 */
public record VerificacionCodigoDto(
        // El email del usuario. No puede estar en blanco y debe tener un formato válido.
        @NotBlank @Email String email,
        // El código de verificación.
        String codigo

) {
}
