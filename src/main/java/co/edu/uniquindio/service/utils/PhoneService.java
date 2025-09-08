package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.ElementoNoValidoException;
import co.edu.uniquindio.exception.ElementoNulosException;

/** Servicio utilitario para el manejo y validación de teléfonos en Store-IT.
   Permite estandarizar el formato de los números telefónicos de acuerdo
   al código de país, garantizando consistencia en los registros.
 */
public interface PhoneService {

    /** Devuelve el número de teléfono formateado de acuerdo al código de país.
       - Lanza ElementoNulosException si el teléfono o código de país son nulos.
       - Lanza ElementoNoValidoException si el formato del número no es válido.
     */
    String obtenerTelefonoFormateado(String telefono, String codigoPais)
            throws ElementoNulosException, ElementoNoValidoException;

}

