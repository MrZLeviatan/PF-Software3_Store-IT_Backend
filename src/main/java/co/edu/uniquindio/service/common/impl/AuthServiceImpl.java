package co.edu.uniquindio.service.common.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.ActualizarPasswordDto;
import co.edu.uniquindio.dto.common.auth.LoginDto;
import co.edu.uniquindio.dto.common.auth.SolicitudEmailDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.security.JWTUtils;
import co.edu.uniquindio.service.common.AuthService;
import co.edu.uniquindio.service.utils.CodigoService;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CodigoService codigoService;
    private final PersonaUtilService personaUtilService;


    @Override
    public void login(LoginDto loginDto)
            throws ElementoNoEncontradoException, ElementoRepetidoException, ElementoIncorrectoException {

        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(loginDto.email());

        if (autentificarPassword(personaOpt, loginDto.password())) {

            // 2. Asignar el código de verificación al usuario.
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            personaOpt.getUser().setCodigo(codigoVerificacion);

            // 3. Enviar código de verificación al email del cliente.
            EmailDto emailDto = new EmailDto(
                    personaOpt.getUser().getEmail(),codigoVerificacion.getClave(),
                    "Código de inicio de Sesión - Store IT!: " + codigoVerificacion.getClave());
            emailService.enviarEmailCodigo(emailDto, "verificacion-login.html");
            personaUtilService.guardarPersonaBD(personaOpt);

        }
    }


    public boolean autentificarPassword(Persona persona, String password)
            throws ElementoRepetidoException, ElementoIncorrectoException {

        // 1. Verificar a la persona y su estado.
        validarEstadoPersona(persona);

        // 2. Verificamos si las credenciales coinciden.
        if (!passwordEncoder.matches(password, persona.getUser().getPassword())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);}

        return true;
    }


    private void validarEstadoPersona(Persona persona) throws ElementoIncorrectoException {

        // 1. Verificar si la cuenta no ha sido activada
        if (persona.getUser().getEstadoCuenta().equals(EstadoCuenta.INACTIVA)){

            // Generamos un nuevo código de verificación
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();

            persona.getUser().setCodigo(codigoVerificacion);

            // Guardamos la nueva asignación de código
            personaUtilService.guardarPersonaBD(persona);

            // Enviar correo con el nuevo código
            EmailDto emailDto = new EmailDto(
                    persona.getUser().getEmail(),
                    codigoVerificacion.getClave(),
                    "Reverificación de Cuenta - Store-It"
            );
            emailService.enviarEmailVerificacionRegistro(emailDto);

            throw new ElementoNoValido(
                    "La cuenta ya existe pero está inactiva. "
                            + "Hemos enviado un nuevo código de verificación a tu correo."
            );
        }

        // 2. Verificar si la cuenta ha sido eliminada
        if (persona.getUser().getEstadoCuenta() == EstadoCuenta.ELIMINADO) {

            // Solo si es un Cliente, lanzar excepción especial para reactivar
            if (persona instanceof Cliente){
                throw new ElementoNoValido(MensajeError.CUENTA_ELIMINADA);}

            // Para otros usuarios (Agente, Personal, RRHH) simplemente cuenta eliminada
            throw new ElementoEliminadoException(MensajeError.CUENTA_ELIMINADA);
        }
    }


    @Override
    public TokenDto verificacionLogin(VerificacionCodigoDto verificacionLoginDto)
            throws ElementoNoEncontradoException, ElementoNoValido {

        String token;

        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(verificacionLoginDto.email());

        codigoService.autentificarCodigo(verificacionLoginDto);

        // 4. Generar y retornar el token JWT con los datos del cliente
            token = jwtUtils.generateToken(personaOpt.getId().toString(),jwtUtils.generarTokenLogin(personaOpt));

        return new TokenDto(token);
    }


    @Override
    public void solicitarRestablecimientoPassword(SolicitudEmailDto solicitudEmailDto)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {

        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(solicitudEmailDto.email());

        validarEstadoPersona(personaOpt);


        // 2. Asignar el código de verificación al usuario.
        Codigo codigoVerificacion = codigoService.generarCodigoRestablecerPassword();
        personaOpt.getUser().setCodigo(codigoVerificacion);

        // 3. Enviar código de verificación al email del cliente.
        EmailDto emailDto = new EmailDto(
                personaOpt.getUser().getEmail(),codigoVerificacion.getClave(),
                "Código de restablecimiento de Contraseña - Store-IT!");
        emailService.enviarEmailCodigo(emailDto,"codigoRestablecerPassword.html");

        personaUtilService.guardarPersonaBD(personaOpt);

    }

    @Override
    public void verificarCodigoPassword(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException {
        codigoService.autentificarCodigo(verificacionCodigoDto);
    }

    @Override
    public void actualizarPassword(ActualizarPasswordDto actualizarPasswordDto)
            throws ElementoNoEncontradoException {

        Persona personaPot = personaUtilService.buscarPersonaPorEmail(actualizarPasswordDto.email());

        // Actualizar la contraseña (encriptándola)
        personaPot.getUser().setPassword(passwordEncoder.encode(actualizarPasswordDto.nuevaPassword()));

        personaUtilService.guardarPersonaBD(personaPot);

    }


}
