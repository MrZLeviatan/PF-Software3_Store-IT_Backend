package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.ElementoNoCoincideException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValidoException;
import co.edu.uniquindio.model.embeddable.Codigo;

/** Servicio encargado de la generación y validación de códigos de seguridad.
   Se utiliza en procesos como la autenticación de dos factores (2FA)
   y el restablecimiento de contraseñas. */
public interface CodigoService {

    // Genera un código único de verificación para procesos de autenticación de dos factores (2FA).
    Codigo generarCodigoVerificacion2AF();

    // Genera un código único para el proceso de restablecimiento de contraseña.
    Codigo generarCodigoRestablecerPassword();

    /** Verifica que el código ingresado coincida con el generado previamente.
       Lanza excepciones si el código no existe o si no coincide. */
    void autentificarCodigo(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException;
}
