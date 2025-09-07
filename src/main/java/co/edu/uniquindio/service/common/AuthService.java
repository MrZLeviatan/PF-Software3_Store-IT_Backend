package co.edu.uniquindio.service.common;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.exception.*;

public interface AuthService {



    void login(LoginDto loginDto)
            throws  ElementoNoEncontradoException, ElementoNoCoincideException,
            ElementoEliminadoException, ElementoNoValidoException;

    GoogleUserResponse loginGoogle(LoginGoogleDto loginGoogleDto)
            throws ElementoNoValidoException, ElementoNoEncontradoException, ElementoEliminadoException;


    TokenDto verificacionLogin(VerificacionCodigoDto verificacionLoginDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException;


    void solicitarRestablecimientoPassword(SolicitudEmailDto solicitudEmailDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException;


    void verificarCodigoPassword(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException;


    void actualizarPassword(ActualizarPasswordDto actualizarPasswordDto)
            throws ElementoNoEncontradoException;


}
