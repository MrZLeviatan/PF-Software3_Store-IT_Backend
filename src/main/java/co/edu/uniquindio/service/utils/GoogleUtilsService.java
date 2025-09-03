package co.edu.uniquindio.service.utils;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleUtilsService {


    GoogleIdToken.Payload verifyIdToken(String idTokenString);

}
