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
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepo clienteRepo;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final PersonaUtilService personaUtilService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final CodigoService codigoService;
    private final GoogleUtilsService googleUtilsService;



    @Override
    public void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        // 0.1. Formateamos los teléfonos
        String telefonoFormateado = phoneService.obtenerTelefonoFormateado(crearClienteDto.telefono(), crearClienteDto.codigoPais());

        // Formateamos el telefono secundario si esta disponible
        String telefonoSecundarioFormateado = null;
        if (crearClienteDto.telefonoSecundario() != null && !crearClienteDto.telefonoSecundario().isEmpty()) {
            telefonoSecundarioFormateado = phoneService.obtenerTelefonoFormateado(crearClienteDto.telefonoSecundario(),
                    crearClienteDto.codigoPaisSecundario());}

        // 1. Validamos que el email y Teléfono no esté registrado
        personaUtilService.validarEmailNoRepetido(crearClienteDto.user().email());
        personaUtilService.validarTelefonoNoRepetido(telefonoFormateado,telefonoSecundarioFormateado);

        // 2. Encriptamos contraseña
        String passwordEncriptada = passwordEncoder.encode(crearClienteDto.user().password());

        // 3. Convertimos DTO a Entidad
        Cliente cliente = clienteMapper.toEntity(crearClienteDto);

        // Validamos el tipo de cliente y si los datos están completos.
        if (cliente.getTipoCliente().equals(TipoCliente.JURIDICO)){
            if (cliente.getNit().isEmpty() || cliente.getNit().isBlank()){
                throw new ElementoNulosException(MensajeError.DATOS_FALTANTES_REGISTRO_PERSONA);}}

        // 4. Seteamos los teléfonos ya formateados
        cliente.setTelefono(telefonoFormateado);
        if (telefonoSecundarioFormateado != null) {
            cliente.setTelefonoSecundario(telefonoSecundarioFormateado);}

        // 5. Asignamos la contraseña encriptada
        cliente.getUser().setPassword(passwordEncriptada);

        // 6. Asignar el código de verificación al usuario.
        Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
        cliente.getUser().setCodigo(codigoVerificacion);

        // 7. Enviar código de verificación al email del cliente.
        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),codigoVerificacion.getClave(),
                "Bienvenido a Store-It - Gestión de Bodegas");

        emailService.enviarEmailVerificacionRegistro(emailDto);

        // 8. Guardamos en la base de datos.
        clienteRepo.save(cliente);
    }


    @Override
    public void verificacionCliente(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNoCoincideException {

        // 1. Obtenemos al cliente con el email proporcionado.
        Cliente cliente = obtenerClientePorEmail(verificacionCodigoDto.email());

        // 2. Verificamos si la cuenta ya está activada
        if (cliente.getUser().getEstadoCuenta().equals(EstadoCuenta.ACTIVO)){
            throw new ElementoIncorrectoException(MensajeError.CUENTA_ACTIVADA);
        }

        // 3. Verificamos la fecha de expiración.
        if (cliente.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())){

            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            cliente.getUser().setCodigo(codigoVerificacion);

            clienteRepo.save(cliente);

            // Enviar correo con el nuevo código
            EmailDto emailDto = new EmailDto(
                    cliente.getUser().getEmail(),
                    codigoVerificacion.getClave(),
                    "Reverificación de Cuenta - Store-It"
            );

            emailService.enviarEmailVerificacionRegistro(emailDto);

            throw new ElementoNoCoincideException(MensajeError.CODIGO_EXPIRADO);
        }

        // 4. Verificamos si el código coincide.
        if (!cliente.getUser().getCodigo().getClave().equals(verificacionCodigoDto.codigo())){
            throw new ElementoNoCoincideException(MensajeError.CODIGO_NO_VALIDO);}


        // 5. Cambiamos el estado de la cuenta del cliente y guardamos en la base de datos.
        cliente.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
        cliente.getUser().setCodigo(null);
        clienteRepo.save(cliente);
    }



    @Override
    public GoogleUserResponse validarToken(String token) throws ElementoIncorrectoException {

        GoogleIdToken.Payload payload = googleUtilsService.verifyIdToken(token);

        if (payload == null){
            throw new ElementoIncorrectoException("Token inválido o expirado");
        }

        // Extraer datos del payload
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        return new GoogleUserResponse(email, name, picture);
    }


    @Override
    public void registroClienteGoogle(CrearClienteGoogleDto crearClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        // 0.1. Formateamos los teléfonos
        String telefonoFormateado = phoneService.obtenerTelefonoFormateado(crearClienteGoogleDto.telefono(), crearClienteGoogleDto.codigoPais());

        // Formateamos el telefono secundario si esta disponible
        String telefonoSecundarioFormateado = null;
        if (crearClienteGoogleDto.telefonoSecundario() != null && !crearClienteGoogleDto.telefonoSecundario().isEmpty()) {
            telefonoSecundarioFormateado = phoneService.obtenerTelefonoFormateado(crearClienteGoogleDto.telefonoSecundario(),
                    crearClienteGoogleDto.codigoPaisSecundario());}

        // 1. Validamos que el email no esté registrado
        personaUtilService.validarEmailNoRepetido(crearClienteGoogleDto.email());

        // 2. Validamos que el teléfono principal no esté registrado
        personaUtilService.validarTelefonoNoRepetido( telefonoFormateado, telefonoSecundarioFormateado);

        // 3. Convertimos el DTO a entidad usando el mapper de Google
        Cliente cliente = clienteMapper.toEntityGoogle(crearClienteGoogleDto);

        // 4. Seteamos los teléfonos ya formateados
        cliente.setTelefono(telefonoFormateado);
        if (telefonoSecundarioFormateado != null) {
            cliente.setTelefonoSecundario(telefonoSecundarioFormateado);}


        //  Encriptamos contraseña
        String passwordEncriptada = passwordEncoder.encode(crearClienteGoogleDto.password());

        cliente.getUser().setPassword(passwordEncriptada);

        // 5. Enviar código de verificación al email del cliente.
        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),null,
                "Bienvenido a Store-It - Gestión de Bodegas");

        emailService.enviarEmailRegistroGoogle(emailDto);

        // 6. Guardamos en la base de datos.
        clienteRepo.save(cliente);
    }



    private Cliente obtenerClientePorEmail(String email)
            throws ElementoNoEncontradoException {
        return clienteRepo.findByUser_Email(email)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
    }

}
