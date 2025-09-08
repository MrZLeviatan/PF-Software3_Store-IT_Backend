package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar
  solicitudes que solo requieren un correo electrónico.
 *
  Su propósito es encapsular y transferir el email del usuario de forma segura y
  eficiente, por ejemplo, para operaciones como la recuperación de contraseña.
  La anotación @NotNull y @Email garantizan que el campo no sea nulo y que
  tenga un formato de correo electrónico válido.
 */
public record SolicitudEmailDto(
        // El email del usuario. No puede ser nulo y debe tener un formato de correo electrónico válido.
        @NotNull @Email String email
) {
}