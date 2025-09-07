package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.entities.users.PersonalBodega;

public interface PersonaUtilService {


    void validarEmailNoRepetido(String email)
            throws ElementoRepetidoException, ElementoEliminadoException;

    void validarTelefonoNoRepetido(String telefono, String telefonoSecundario)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoValidoException;

    Persona buscarPersonaPorEmail(String email)
            throws ElementoNoEncontradoException;

    void guardarPersonaBD (Persona persona);


    PersonalBodega obtenerPersonalBodetaEmail(String email) throws ElementoNoEncontradoException;

}
