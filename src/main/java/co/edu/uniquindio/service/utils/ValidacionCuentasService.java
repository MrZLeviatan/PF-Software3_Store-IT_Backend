package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.ElementoEliminadoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;

public interface ValidacionCuentasService {


    void validarEmailNoRepetido(String email)
            throws ElementoRepetidoException, ElementoEliminadoException;

    void validarTelefonoNoRepetido(String telefono, String telefonoSecundario)
            throws ElementoRepetidoException;



}
