package co.edu.uniquindio.dto;

import lombok.Getter;
import lombok.Setter;

/** Este es un Data Transfer Object (DTO) simple para manejar tokens de autenticación.
  Su propósito es encapsular un token, facilitando su transferencia entre diferentes
  capas de la aplicación, como desde el servicio de autenticación al controlador.
 */
@Getter
@Setter
public class TokenDto {
    // Almacena el token de autenticación.
    private String token;

    // Constructor que inicializa el DTO con el token.
    public TokenDto(String token) {this.token = token;}

}
