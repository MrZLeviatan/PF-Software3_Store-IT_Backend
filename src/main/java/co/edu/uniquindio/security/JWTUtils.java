package co.edu.uniquindio.security;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.entities.users.RecursosHumanos;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * Clase utilitaria para generar y validar tokens JWT.
 * Esta clase es responsable de crear tokens JWT con una duración determinada
 * y de validar la autenticidad de los mismos.
 * <p>
 * El token JWT es utilizado para la autenticación y autorización del usuario en el sistema.
 * Los métodos de esta clase permiten generar tokens con una fecha de expiración establecida,
 * y también validar los tokens al ser enviados por el cliente para asegurar que son válidos.
 * </p>
 */
@Component
public class JWTUtils {


    // Clave secreta usada para firmar los tokens (mínimo 256 bits para HS256)
    private static final String SECRET = "store-it-secret-key-para-firmar-tokens-jwt-de-forma-segura";



    // Genera un token JWT con los datos proporcionados. Este token es utilizado para autenticar y autorizar al usuario en el sistema.
    public String generateToken(String id, Map<String, String> claims) {

        // Se obtiene el instante actual para usar como fecha de emisión y para la expiración
        Instant now = Instant.now(); // Se obtiene el instante actual

        // Se construye el token JWT con los datos proporcionados y firma con una clave secreta
        return Jwts.builder()
                .subject(id) // ID del usuario
                .issuedAt(Date.from(now)) // Fecha de emisión
                .expiration(Date.from(now.plus(1L, ChronoUnit.HOURS))) // Expira en 1 hora
                .claims(claims) // Añade los claims personalizados como el tipo de usuario
                .signWith(getKey(), Jwts.SIG.HS256) // Firma el token con HS256
                .compact();
    }


    public Map<String,String> generarTokenLogin(Persona persona)
            throws ElementoNoEncontradoException {

        // Mapa que asocia las clases concretas con su respectivo rol en formato ROLE_*
        Map<Class<?>, String> rolesPorClase = Map.of(
                Cliente.class, "ROLE_CLIENTE",
                RecursosHumanos.class, "ROLE_RECURSOS_HUMANOS");

        // Obtener el rol correspondiente a la clase específica del objeto persona
        String rol;

        if (persona instanceof PersonalBodega){
            rol = ((PersonalBodega) persona).getRol();
        }else{
            rol = rolesPorClase.get(persona.getClass());
        }

        // Validar que el rol exista; si no, lanzar excepción indicando que no se encontró el rol
        if (rol == null || rol.isEmpty()) {
            throw new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ROL);}

        // Retornar un mapa con los datos del token: email, nombre y rol
        return Map.of(
                "email", persona.getUser().getEmail(),
                "nombre", persona.getNombre(),
                "rol", rol);
    }


    // Válida y analiza un token JWT recibido.
    public Jws<Claims> parseJwt(String jwtString) throws JwtException {
        JwtParser parser = Jwts.parser()
                .verifyWith(getKey()) // Verifica la firma
                .build();

        return parser.parseSignedClaims(jwtString); // Devuelve los claims seguros (firmados)
    }


    // Obtiene la clave secreta HMAC usada para firmar/verificar los tokens.
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

}
