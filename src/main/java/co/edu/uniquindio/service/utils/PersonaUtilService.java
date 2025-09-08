package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.entities.users.PersonalBodega;

/** Servicio utilitario para la gestión de validaciones y consultas relacionadas
   con entidades de tipo Persona y PersonalBodega en Store-IT.
   Su objetivo es garantizar la integridad de la información de contacto
   y la correcta administración de usuarios en la base de datos. */
public interface PersonaUtilService {

    /** Valida que el correo electrónico no se encuentre repetido en el sistema.
       - Lanza ElementoRepetidoException si el email ya está registrado.
       - Lanza ElementoEliminadoException si el email corresponde a un usuario eliminado. */
    void validarEmailNoRepetido(String email)
            throws ElementoRepetidoException, ElementoEliminadoException;

    /** Valida que los teléfonos (principal y secundario) no estén repetidos
       en el sistema y cumplan con los requisitos de formato.
       - Lanza ElementoRepetidoException si alguno ya existe en otro registro.
       - Lanza ElementoNulosException si alguno de los valores es nulo.
       - Lanza ElementoNoValidoException si el formato no es válido. */
    void validarTelefonoNoRepetido(String telefono, String telefonoSecundario)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoValidoException;

    /** Busca y devuelve una persona registrada según su correo electrónico.
       - Lanza ElementoNoEncontradoException si no existe ninguna persona con ese email. */
    Persona buscarPersonaPorEmail(String email)
            throws ElementoNoEncontradoException;

    /* Persiste una entidad Persona en la base de datos. */
    void guardarPersonaBD(Persona persona);

    /** Obtiene un usuario de tipo PersonalBodega a partir de su correo electrónico.
       - Lanza ElementoNoEncontradoException si no existe un registro con el email dado. */
    PersonalBodega obtenerPersonalBodetaEmail(String email)
            throws ElementoNoEncontradoException;

}

