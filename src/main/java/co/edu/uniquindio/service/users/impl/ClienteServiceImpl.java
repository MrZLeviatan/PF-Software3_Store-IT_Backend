package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.VerificacionClienteDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.users.ClienteService;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.PhoneService;
import co.edu.uniquindio.service.utils.ValidacionCuentasService;
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
    private final ValidacionCuentasService validacionCuentasService;
    private final PhoneService phoneService;
    private final EmailService emailService;



    @Override
    public void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException {

        // 1. Validamos que el email y Teléfono no esté registrado
        validacionCuentasService.validarEmailNoRepetido(crearClienteDto.user().email());
        validacionCuentasService.validarTelefonoNoRepetido(crearClienteDto.telefono(),crearClienteDto.telefonoSecundario());

        // 2. Encriptamos contraseña
        String passwordEncriptada = passwordEncoder.encode(crearClienteDto.user().password());

        // 3. Convertimos DTO a Entidad
        Cliente cliente = clienteMapper.toEntity(crearClienteDto);

        // 4. Formateamos los teléfonos
        cliente.setTelefono(phoneService.obtenerTelefonoFormateado(cliente.getTelefono(),crearClienteDto.codigoPais()));

        // Formateamos el teléfono secundario si esta disponible.
        if (cliente.getTelefonoSecundario() != null && !cliente.getTelefonoSecundario().isEmpty()) {
            cliente.setTelefonoSecundario(phoneService.obtenerTelefonoFormateado(cliente.getTelefonoSecundario(), crearClienteDto.codigoPaisSecundario()));}

        // 5. Asignamos la contraseña encriptada
        cliente.getUser().setPassword(passwordEncriptada);

        // 6. Genera el código de verificación.
        String verificacion = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // 7. Asignar el código de verificación al usuario.
        Codigo codigoVerificacion = new Codigo();
        codigoVerificacion.setClave(verificacion);
        codigoVerificacion.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
        codigoVerificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        cliente.getUser().setCodigo(codigoVerificacion);

        // 8. Enviar código de verificación al email del cliente.
        EmailDto emailDto = new EmailDto(
                cliente.getUser().getEmail(),verificacion,
                "Bienvenido a Store-It - Gestión de Bodegas");

        emailService.enviarEmailVerificacion(emailDto);

        // 9. Guardamos en la base de datos.
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


    private Cliente obtenerClientePorEmail(String email) throws ElementoNoEncontradoException {
        return clienteRepo.findByUser_Email(email)
                .orElseThrow(() ->
                        new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO + email));
    }

}
