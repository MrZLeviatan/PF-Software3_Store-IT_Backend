package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.dto.users.cliente.VerificacionClienteDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCliente;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.users.ClienteService;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.PhoneService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
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



    @Override
    public void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException {

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
        Codigo codigoVerificacion = codigoService.generarCodigoVerificacionRegistro();
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
    public void verificacionCliente(VerificacionClienteDto verificacionClienteDto)
            throws ElementoNoEncontradoException {

        // 1. Obtenemos al cliente con el email proporcionado.
        Cliente cliente = obtenerClientePorEmail(verificacionClienteDto.email());

        // 2. Verificamos la fecha de expiración.
        if (cliente.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new ElementoNoValido(MensajeError.CODIGO_EXPIRADO);}

        // 3. Verificamos si el código coincide.
        if (!cliente.getUser().getCodigo().getClave().equals(verificacionClienteDto.codigo())){
            throw new ElementoNoValido(MensajeError.CODIGO_NO_VALIDO);}

        // 4. Cambiamos el estado de la cuenta del cliente y guardamos en la base de datos.
        cliente.getUser().setEstadoCuenta(EstadoCuenta.ACTIVO);
        clienteRepo.save(cliente);
    }



    @Override
    public void registroClienteGoogle(CrearClienteGoogleDto crearClienteGoogleDto) throws ElementoRepetidoException, ElementoNulosException {

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

        // 4.1 Por obligación se quema una contraseña para el usuario
        cliente.getUser().setPassword(passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 6).toUpperCase()));

        // 5. Enviar código de verificación al email del cliente.
        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),null,
                "Bienvenido a Store-It - Gestión de Bodegas");

        emailService.enviarEmailRegistroGoogle(emailDto);

        // 6. Guardamos en la base de datos.
        clienteRepo.save(cliente);
    }



    private Cliente obtenerClientePorEmail(String email) throws ElementoNoEncontradoException {
        return clienteRepo.findByUser_Email(email)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO + email));
    }

}
