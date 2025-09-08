package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.*;

/**
 // Interfaz del servicio que maneja la gestión de clientes en Store-IT.
 // Define los métodos principales para el registro, validación y verificación
 // de clientes tanto de forma tradicional como mediante Google.
 */
public interface ClienteService {

    /** Registra un nuevo cliente en el sistema.
    // Puede lanzar excepciones si:
    // - El cliente ya existe.
    // - Algún campo es nulo.
    // - El cliente ya fue eliminado previamente.
    // - Los datos no son válidos.
     */
    void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException,
            ElementoEliminadoException, ElementoNoValidoException;

    /** Verifica un cliente a través de un código de verificación.
    // Puede fallar si:
    // - El cliente no se encuentra.
    // - El código es incorrecto.
    // - Los datos no coinciden.
     */
    void verificacionCliente(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNoCoincideException;

    // Valida un token de Google para autenticar al cliente.
    // Devuelve la información del usuario si el token es válido.
    GoogleUserResponse validarToken(String token) throws ElementoIncorrectoException;

    // Registra un cliente utilizando sus credenciales de Google.
    // Puede lanzar excepciones similares al registro tradicional.
    void registroClienteGoogle(CrearClienteGoogleDto crearClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException,
            ElementoEliminadoException, ElementoNoValidoException;

}
