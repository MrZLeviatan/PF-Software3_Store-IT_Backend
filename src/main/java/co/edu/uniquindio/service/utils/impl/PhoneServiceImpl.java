package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.ElementoNoValidoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.service.utils.PhoneService;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Service;

@Service
public class PhoneServiceImpl implements PhoneService {

    // Utilidad de Google para trabajar con números telefónicos
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    public String obtenerTelefonoFormateado(String telefono, String codigoPais)
            throws ElementoNulosException, ElementoNoValidoException {

        // Validamos que el código del país no sea nulo o vacío
        if (codigoPais.isEmpty()) {
            throw new ElementoNulosException(MensajeError.PAIS_NO_ENCONTRADO);
        }

        // Verificamos que el número sea válido para el país dado
        if (!validarTelefono(telefono, codigoPais)) {
            throw new ElementoNoValidoException(MensajeError.TELEFONO_INVALIDO);
        }

        // Si todo está correcto, se retorna el número en formato internacional
        return formatearTelefono(telefono, codigoPais);
    }

    // Método que comprueba si un número es válido según el país
    public boolean validarTelefono(String telefono, String codigoPais) throws ElementoNoValidoException {
        try {
            Phonenumber.PhoneNumber numero = phoneNumberUtil.parse(telefono, codigoPais);
            return phoneNumberUtil.isValidNumber(numero); // True si es válido
        } catch (NumberParseException e) {
            // Se lanza excepción personalizada si no se puede parsear el número
            throw new ElementoNoValidoException(MensajeError.TELEFONO_INVALIDO + e.getMessage());
        }
    }

    // Método que da formato internacional al número (ejemplo: +57 300 1234567)
    public String formatearTelefono(String telefono, String codigoPais) throws ElementoNoValidoException {
        try {
            Phonenumber.PhoneNumber numero = phoneNumberUtil.parse(telefono, codigoPais);
            return phoneNumberUtil.format(numero, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            // Se lanza excepción si el número no se puede formatear
            throw new ElementoNoValidoException(MensajeError.TELEFONO_INVALIDO + e.getMessage());
        }
    }
}
