package co.edu.uniquindio.service.common.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.LoginDto;
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

import java.time.LocalDateTime;

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

            // 7. Asignar el código de verificación al usuario.
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacion2AF();
            personaOpt.getUser().setCodigo(codigoVerificacion);

            // 8. Enviar código de verificación al email del cliente.
            EmailDto emailDto = new EmailDto(
                    personaOpt.getUser().getEmail(),codigoVerificacion.getClave(),
                    "Código de inicio de Sesión - Store IT!: " + codigoVerificacion);
            emailService.enviarEmailVerificacionLogin(emailDto);
            personaUtilService.guardarPersonaBD(personaOpt);

        }
    }


    public boolean autentificarPassword(Persona persona, String password)
            throws ElementoRepetidoException, ElementoIncorrectoException {

        // 1. Verificar si la cuenta no ha sido activada

        if (persona.getUser().getEstadoCuenta().equals(EstadoCuenta.INACTIVA)){

            // Generamos un nuevo código de verificación
            Codigo codigoVerificacion = codigoService.generarCodigoVerificacionRegistro();

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

        if (!passwordEncoder.matches(password, persona.getUser().getPassword())) {
            throw new ElementoIncorrectoException(MensajeError.PASSWORD_INCORRECTO);}

        return true;
    }


    @Override
    public TokenDto verificacionLogin(VerificacionCodigoDto verificacionLoginDto)
            throws ElementoNoEncontradoException, ElementoNoValido {

        String token;

        Persona personaOpt = personaUtilService.buscarPersonaPorEmail(verificacionLoginDto.email());

        // 2. Verificamos la fecha de expiración.
        if (personaOpt.getUser().getCodigo().getFechaExpiracion().isBefore(LocalDateTime.now())){
            throw new ElementoNoValido(MensajeError.CODIGO_EXPIRADO);}

        // 3. Verificamos si el código coincide.
        if (!personaOpt.getUser().getCodigo().getClave().equals(verificacionLoginDto.codigo())){
            throw new ElementoNoValido(MensajeError.CODIGO_NO_VALIDO);}

        // 4. Generar y retornar el token JWT con los datos del cliente
            token = jwtUtils.generateToken(personaOpt.getId().toString(),jwtUtils.generarTokenLogin(personaOpt));

        return new TokenDto(token);
    }


}
