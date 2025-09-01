package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.service.utils.CodigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CodigoServiceImpl implements CodigoService {



    @Override
    public Codigo generarCodigoVerificacion2AF() {

        Codigo codigoVerificacion = new Codigo();
        codigoVerificacion.setClave(generacionClave());
        codigoVerificacion.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigoVerificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(10));

        return codigoVerificacion;
    }

    @Override
    public Codigo generarCodigoVerificacionRegistro() {

        Codigo codigoVerificacion = new Codigo();
        codigoVerificacion.setClave(generacionClave());
        codigoVerificacion.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigoVerificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));

        return codigoVerificacion;
    }


    private String generacionClave(){
        return UUID.randomUUID().toString()
                        .replace("-", "") // Eliminamos guiones para mayor limpieza
                        .substring(0, 6)
                        .toUpperCase();
    }



}
