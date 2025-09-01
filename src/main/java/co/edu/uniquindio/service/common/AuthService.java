package co.edu.uniquindio.service.common;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.ActualizarPasswordDto;
import co.edu.uniquindio.dto.common.auth.LoginDto;
import co.edu.uniquindio.dto.common.auth.SolicitudEmailDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.ElementoEliminadoException;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;

public interface AuthService {



    void login(LoginDto loginDto)
            throws ElementoRepetidoException, ElementoNoEncontradoException, ElementoIncorrectoException, ElementoEliminadoException;


    TokenDto verificacionLogin(VerificacionCodigoDto verificacionLoginDto)
            throws ElementoNoEncontradoException;


    void solicitarRestablecimientoPassword(SolicitudEmailDto solicitudEmailDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoEliminadoException;


    void verificarCodigoPassword(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException;


    void actualizarPassword(ActualizarPasswordDto actualizarPasswordDto)
            throws ElementoNoEncontradoException;


}
