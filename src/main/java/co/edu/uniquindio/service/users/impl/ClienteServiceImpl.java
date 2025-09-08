package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.users.ClienteService;
import co.edu.uniquindio.service.utils.*;
import co.edu.uniquindio.service.utils.impl.GoogleUtilsServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 // Implementación de ClienteService.
 // Gestiona:
 // - Registro de clientes (normal y con Google)
 // - Verificación de cuenta con código
 // - Validación de token Google
 */
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    /* Dependencias principales */
    private final ClienteRepo clienteRepo;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final PersonaUtilService personaUtilService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final CodigoService codigoService;
    private final GoogleUtilsService googleUtilsService;

    /**
     // Registrar un cliente con datos tradicionales.
     // - Valida correos y teléfonos
     // - Encripta contraseña
     // - Asigna código de verificación
     // - Envía correo y guarda en la base de datos
     */
    @Override
    public void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        String telefonoFormateado = phoneService.obtenerTelefonoFormateado(
                crearClienteDto.telefono(), crearClienteDto.codigoPais());

        String telefonoSecundarioFormateado = null;
        if (crearClienteDto.telefonoSecundario() != null && !crearClienteDto.telefonoSecundario().isEmpty()) {
            telefonoSecundarioFormateado = phoneService.obtenerTelefonoFormateado(
                    crearClienteDto.telefonoSecundario(), crearClienteDto.codigoPaisSecundario());
        }

        personaUtilService.validarEmailNoRepetido(crearClienteDto.user().email());
        personaUtilService.validarTelefonoNoRepetido(telefonoFormateado, telefonoSecundarioFormateado);

        String passwordEncriptada = passwordEncoder.encode(crearClienteDto.user().password());

        Cliente cliente = clienteMapper.toEntity(crearClienteDto);

        if (cliente.getTipoCliente().equals(TipoCliente.JURIDICO) &&
                (cliente.getNit().isEmpty() || cliente.getNit().isBlank())) {
            throw new ElementoNulosException(MensajeError.DATOS_FALTANTES_REGISTRO_PERSONA);
        }

        cliente.setTelefono(telefonoFormateado);
        if (telefonoSecundarioFormateado != null) {
            cliente.setTelefonoSecundario(telefonoSecundarioFormateado);
        }

        cliente.getUser().setPassword(passwordEncriptada);

        Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
        cliente.getUser().setCodigo(codigoVerificacion);

        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),
                codigoVerificacion.getClave(),
                "Bienvenido a Store-It - Gestión de Bodegas");
        emailService.enviarEmailVerificacionRegistro(emailDto);

        clienteRepo.save(cliente);
    }

    /**
     // Verificación de cuenta con un código.
     // - Verifica si ya está activa
     // - Revisa expiración del código
     //- Activa la cuenta si el código es correcto
     */
    @Override
    public void verificacionCliente(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNoCoincideException {

        Cliente cliente = obtenerClientePorEmail(verificacionCodigoDto.email());

        if (cliente.getUser().getEstadoCuenta().equals(EstadoCuenta.ACTIVO)) {
            throw new ElementoIncorrectoException(MensajeError.CUENTA_ACTIVADA);
        }

        if (cliente.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            cliente.getUser().setCodigo(codigoVerificacion);
            clienteRepo.save(cliente);

            EmailDto emailDto = new EmailDto(
                    cliente.getUser().getEmail(),
                    codigoVerificacion.getClave(),
                    "Reverificación de Cuenta - Store-It");
            emailService.enviarEmailVerificacionRegistro(emailDto);

            throw new ElementoNoCoincideException(MensajeError.CODIGO_EXPIRADO);
        }

        if (!cliente.getUser().getCodigo().getClave().equals(verificacionCodigoDto.codigo())) {
            throw new ElementoNoCoincideException(MensajeError.CODIGO_NO_VALIDO);
        }

        cliente.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
        cliente.getUser().setCodigo(null);
        clienteRepo.save(cliente);
    }

    /**
     // Validar un token de Google.
     //- Verifica validez
     // - Extrae datos de usuario (email, nombre, foto)
     */
    @Override
    public GoogleUserResponse validarToken(String token) throws ElementoIncorrectoException {

        GoogleIdToken.Payload payload = googleUtilsService.verifyIdToken(token);

        if (payload == null) {
            throw new ElementoIncorrectoException("Token inválido o expirado");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        return new GoogleUserResponse(email, name, picture);
    }

    /**
     // Registro de cliente con cuenta de Google.
     // - Valida correo y teléfonos
     // - Convierte DTO a entidad
     // - Encripta contraseña
     // - Envía correo de bienvenida
     */
    @Override
    public void registroClienteGoogle(CrearClienteGoogleDto crearClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        String telefonoFormateado = phoneService.obtenerTelefonoFormateado(
                crearClienteGoogleDto.telefono(), crearClienteGoogleDto.codigoPais());

        String telefonoSecundarioFormateado = null;
        if (crearClienteGoogleDto.telefonoSecundario() != null && !crearClienteGoogleDto.telefonoSecundario().isEmpty()) {
            telefonoSecundarioFormateado = phoneService.obtenerTelefonoFormateado(
                    crearClienteGoogleDto.telefonoSecundario(), crearClienteGoogleDto.codigoPaisSecundario());
        }

        personaUtilService.validarEmailNoRepetido(crearClienteGoogleDto.email());
        personaUtilService.validarTelefonoNoRepetido(telefonoFormateado, telefonoSecundarioFormateado);

        Cliente cliente = clienteMapper.toEntityGoogle(crearClienteGoogleDto);

        cliente.setTelefono(telefonoFormateado);
        if (telefonoSecundarioFormateado != null) {
            cliente.setTelefonoSecundario(telefonoSecundarioFormateado);
        }

        String passwordEncriptada = passwordEncoder.encode(crearClienteGoogleDto.password());
        cliente.getUser().setPassword(passwordEncriptada);

        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),
                null,
                "Bienvenido a Store-It - Gestión de Bodegas");
        emailService.enviarEmailRegistroGoogle(emailDto);

        clienteRepo.save(cliente);
    }

    /*
     * Obtener un cliente desde el email.
     * Lanza excepción si no existe.
     */
    private Cliente obtenerClientePorEmail(String email)
            throws ElementoNoEncontradoException {
        return clienteRepo.findByUser_Email(email)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
    }
}
