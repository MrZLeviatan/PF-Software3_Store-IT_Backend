package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.model.embeddable.Codigo;
import org.springframework.stereotype.Service;


public interface CodigoService {


    Codigo generarCodigoVerificacion2AF();

    Codigo generarCodigoVerificacionRegistro();
}
