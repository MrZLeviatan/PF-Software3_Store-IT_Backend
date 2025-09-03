package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.service.utils.GoogleUtilsService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Component
public class GoogleUtilsServiceImpl implements GoogleUtilsService {


    // ✅ Este es el CLIENT_ID que obtienes de Google Cloud Console
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;


    private final GoogleIdTokenVerifier verifier;


    public GoogleUtilsServiceImpl() {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
    }


    /**
     * Verifica la validez de un ID Token de Google
     * @param idTokenString token recibido desde el frontend
     * @return payload con datos del usuario o null si no es válido
     */
    @Override
    public GoogleIdToken.Payload verifyIdToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload(); // Contiene email, nombre, foto, etc.
            } else {
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
