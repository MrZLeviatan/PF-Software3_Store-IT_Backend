package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.service.utils.PhoneService;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Service;

@Service
public class PhoneServiceImpl implements PhoneService {

    // Instancia del validador de números telefónicos de la librería de Google
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();


    @Override
    public String obtenerTelefonoFormateado(String telefono, String codigoPais) throws ElementoNulosException {

        // Intentamos obtener el código ISO del país a partir del nombre proporcionado

        // Verificamos que el código del país sea válido
        if (codigoPais.isEmpty()) {
            throw new ElementoNulosException(MensajeError.PAIS_NO_ENCONTRADO);}

        // Validamos que el número coincida con el formato y reglas del país
        if (!validarTelefono(telefono,codigoPais)) {
            throw new ElementoNoValido(MensajeError.TELEFONO_INVALIDO);}

        // Si todo es válido, devolvemos el número formateado
        return formatearTelefono(telefono, codigoPais);
    }


    public boolean validarTelefono(String telefono, String codigoPais) {
        try {
            Phonenumber.PhoneNumber numero = phoneNumberUtil.parse(telefono, codigoPais);
            return phoneNumberUtil.isValidNumber(numero); // Devuelve true si el número es válido
        } catch (NumberParseException e) {
            // Lanza una excepción personalizada si el número es inválido
            throw new ElementoNoValido(MensajeError.TELEFONO_INVALIDO + e.getMessage());
        }}



    public String formatearTelefono(String telefono, String codigoPais) {
        try {
            Phonenumber.PhoneNumber numero = phoneNumberUtil.parse(telefono, codigoPais);
            return phoneNumberUtil.format(numero, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            // Retorna si el número no es válido
            throw new ElementoNoValido(MensajeError.TELEFONO_INVALIDO + e.getMessage());
        }}


}
