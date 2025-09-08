package co.edu.uniquindio.dto.common.user;

import co.edu.uniquindio.model.enums.EstadoCuenta;
import jakarta.validation.constraints.Email;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para representar la información
  de un usuario.
 *
  Su propósito es transferir datos de usuario, como el email, la contraseña, el estado de la cuenta
  y el código de restablecimiento, de forma segura entre las capas de la aplicación.
  Este DTO es útil para operaciones de visualización, evitando exponer detalles innecesarios de
  la entidad completa.
 */
public record UserDto(
        // El email del usuario. Debe tener un formato de correo válido.
        @Email String email,
        // La contraseña del usuario.
        String password,
        // El estado actual de la cuenta del usuario (ej: ACTIVA, INACTIVA).
        EstadoCuenta estadoCuenta,
        // Objeto DTO que contiene el código de restablecimiento y su fecha de expiración.
        CodigoDto codigoRestablecimiento
) {
}