package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.NotBlank;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar
  el token de autenticación de Google al iniciar sesión.
 *
  Su propósito es encapsular el token de identificación (idToken) proporcionado por Google,
  que se utiliza para verificar la identidad del usuario y autenticarlo en el sistema.
  La anotación @NotBlank garantiza que el token no esté vacío, lo cual es esencial
  para el proceso de autenticación.
 */
public record LoginGoogleDto(
        // El token de autenticación de Google. No puede estar en blanco.
        @NotBlank String idToken
) {
}
