package co.edu.uniquindio.dto.common.google;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar la
  información de un usuario devuelta por el servicio de autenticación de Google.
 *
  Su propósito es encapsular los datos básicos del usuario, como su email, nombre y
  URL de la imagen de perfil, de manera segura y eficiente. Este DTO simplifica la
  extracción de la información relevante del token de Google para su uso en la
  creación o actualización de perfiles de usuario en la aplicación.
 */
public record GoogleUserResponse(
        // El email del usuario.
        String email,
        // El nombre completo del usuario.
        String name,
        // La URL de la imagen de perfil del usuario.
        String picture
) {
}