package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CodigoServiceImpl implements CodigoService {


    private final PersonaUtilService personaUtilService;


    @Override
    public Codigo generarCodigoVerificacion2AF() {

        Codigo codigoVerificacion = new Codigo();
        codigoVerificacion.setClave(generacionClave());
        codigoVerificacion.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigoVerificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));

        return codigoVerificacion;
    }

    @Override
    public Codigo generarCodigoRestablecerPassword() {

        Codigo codigoRestablecerPassword = new Codigo();
        codigoRestablecerPassword.setClave(generacionClave());
        codigoRestablecerPassword.setTipoCodigo(TipoCodigo.RESTABLECER_PASSWORD);
        codigoRestablecerPassword.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));

        return codigoRestablecerPassword;
    }


    @Override
    public void autentificarCodigo(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException {

        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(verificacionCodigoDto.email());

        // 2. Verificamos la fecha de expiración.
        if (personaOpt.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())){
            throw new ElementoNoValido(MensajeError.CODIGO_EXPIRADO);
        }

        // 3. Verificamos si el código coincide.
        if (!personaOpt.getUser().getCodigo().getClave().equals(verificacionCodigoDto.codigo())){
            throw new ElementoNoValido(MensajeError.CODIGO_NO_VALIDO);}

        // Limpiar el código de restablecimiento
        personaOpt.getUser().setCodigo(null);

        personaUtilService.guardarPersonaBD(personaOpt);
    }


    private String generacionClave(){
        return UUID.randomUUID().toString()
                        .replace("-", "") // Eliminamos guiones para mayor limpieza
                        .substring(0, 6)
                        .toUpperCase();
    }



}
