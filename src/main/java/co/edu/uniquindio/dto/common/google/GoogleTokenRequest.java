package co.edu.uniquindio.dto.common.google;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para manejar
  las solicitudes de token de Google.
 *
  Su propósito es encapsular y transferir el token de identificación (idToken)
  proporcionado por Google. Es una estructura de datos simple y ligera
  que se utiliza para enviar la información del token a un servicio de validación
  en el backend, permitiendo la autenticación a través de Google.
 */
public record GoogleTokenRequest(

        // El token de identificación de Google.
        String idToken

) {
}