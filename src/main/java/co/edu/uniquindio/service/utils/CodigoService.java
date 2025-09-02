package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.ElementoNoCoincideException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValidoException;
import co.edu.uniquindio.model.embeddable.Codigo;


public interface CodigoService {


    Codigo generarCodigoVerificacion2AF();


    Codigo generarCodigoRestablecerPassword();

    void autentificarCodigo(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException;
}
