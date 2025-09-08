package co.edu.uniquindio.service.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

/** Servicio utilitario para la integración con Google Identity.
   Permite verificar la validez de un ID Token emitido por Google
   y obtener la información del usuario autenticado. */
public interface GoogleUtilsService {

    /** Verifica un ID Token de Google y retorna su información decodificada (payload).
       - Retorna un objeto GoogleIdToken.Payload si el token es válido.
       - Retorna null si el token no es válido o ha expirado. */
    GoogleIdToken.Payload verifyIdToken(String idTokenString);

}

