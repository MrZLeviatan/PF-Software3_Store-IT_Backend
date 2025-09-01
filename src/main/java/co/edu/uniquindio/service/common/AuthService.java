package co.edu.uniquindio.service.common;

import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.LoginDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;

public interface AuthService {



    void login(LoginDto loginDto) throws ElementoRepetidoException, ElementoNoEncontradoException, ElementoIncorrectoException;







}
