package co.edu.uniquindio.service.common;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.exception.*;

public interface AuthService {

    // Inicia sesión con credenciales tradicionales (usuario y contraseña)
    void login(LoginDto loginDto)
            throws  ElementoNoEncontradoException, ElementoNoCoincideException,
            ElementoEliminadoException, ElementoNoValidoException;

    // Inicia sesión con una cuenta de Google
    GoogleUserResponse loginGoogle(LoginGoogleDto loginGoogleDto)
            throws ElementoNoValidoException, ElementoNoEncontradoException, ElementoEliminadoException;

    // Verifica el código enviado para completar el login (2FA o similar)
    TokenDto verificacionLogin(VerificacionCodigoDto verificacionLoginDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException;

    // Solicita el envío de un correo para restablecer la contraseña
    void solicitarRestablecimientoPassword(SolicitudEmailDto solicitudEmailDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException;

    // Verifica que el código ingresado para restablecer la contraseña sea válido
    void verificarCodigoPassword(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException;

    // Actualiza la contraseña del usuario
    void actualizarPassword(ActualizarPasswordDto actualizarPasswordDto)
            throws ElementoNoEncontradoException;

}

