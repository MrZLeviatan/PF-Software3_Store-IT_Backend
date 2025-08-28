package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.ElementoNulosException;

public interface PhoneService {
    

    String obtenerTelefonoFormateado (String telefono, String codigoPais)
            throws ElementoNulosException;


    
}
