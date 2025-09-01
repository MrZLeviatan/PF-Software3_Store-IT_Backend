package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.exception.ElementoEliminadoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValido;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.entities.users.RecursosHumanos;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.repository.users.RecursosHumanosRepo;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PersonaUtilServiceImpl implements PersonaUtilService {

    private final ClienteRepo clienteRepo;
    private final PersonalBodegaRepo personalBodegaRepo;
    private final RecursosHumanosRepo recursosHumanosRepo;
    private final EmailService emailService;



    @Override
    public void validarEmailNoRepetido(String email)
            throws ElementoRepetidoException, ElementoEliminadoException {

        //  Se busca el email en el repositorio de cada entidad
        Optional<? extends Persona> personaOpt =
                clienteRepo.findByUser_Email(email)
                        .map(c -> (Persona) c) // Sí se encuentra el email, se castea el objeto
                        .or(() -> personalBodegaRepo.findByUser_Email(email))
                        .or(() -> recursosHumanosRepo.findByUser_Email(email));

        if (personaOpt.isPresent()) {
            Persona persona = personaOpt.get();

            //  Caso 1: Cuenta eliminada -> no se puede volver a usar
            if (persona.getUser().getEstadoCuenta().equals(EstadoCuenta.ELIMINADO)) {
                throw new ElementoEliminadoException(MensajeError.EMAIL_CUENTA_ELIMINADA);
            }

            //  Caso 2: Cuenta activa -> email ya registrado y en uso
            if (persona.getUser().getEstadoCuenta().equals(EstadoCuenta.ACTIVO)) {
                throw new ElementoRepetidoException(MensajeError.EMAIL_YA_EXISTE);
            }

            //  Caso 3: Cuenta inactiva -> reenviar código de verificación
            if (persona.getUser().getEstadoCuenta().equals(EstadoCuenta.INACTIVA)) {
                // Generamos un nuevo código de verificación
                String verificacion = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

                Codigo codigoVerificacion = new Codigo();
                codigoVerificacion.setClave(verificacion);
                codigoVerificacion.setTipoCodigo(TipoCodigo.VERIFICACION_2FA);
                codigoVerificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));

                persona.getUser().setCodigo(codigoVerificacion);

                // Guardamos la nueva asignación de código
                clienteRepo.save((Cliente) persona);

                // Enviar correo con el nuevo código
                EmailDto emailDto = new EmailDto(
                        persona.getUser().getEmail(),
                        verificacion,
                        "Reverificación de Cuenta - Store-It"
                );
                emailService.enviarEmailVerificacionRegistro(emailDto);

                throw new ElementoRepetidoException(
                        "La cuenta ya existe pero está inactiva. "
                                + "Hemos enviado un nuevo código de verificación a tu correo."
                );
            }
        }
    }


    @Override
    public void validarTelefonoNoRepetido(String telefono, String telefonoSecundario)
            throws ElementoRepetidoException, ElementoNoValido {

        // Si el teléfono principal es nulo, no se puede validar
        if (telefono == null || telefono.isBlank()) {
            throw new ElementoNoValido(MensajeError.TELEFONO_VACIO);
        }

        boolean existe;

        if (telefonoSecundario == null || telefonoSecundario.isBlank()) {
            // Solo validar el teléfono principal
            existe =
                    clienteRepo.existsByTelefono(telefono) ||
                            recursosHumanosRepo.existsByTelefono(telefono) ||
                            personalBodegaRepo.existsByTelefono(telefono);
        } else {
            // Validar ambos teléfonos
            existe =
                    clienteRepo.existsByTelefonoOrTelefonoSecundario(telefono, telefonoSecundario) ||
                            recursosHumanosRepo.existsByTelefonoOrTelefonoSecundario(telefono, telefonoSecundario) ||
                            personalBodegaRepo.existsByTelefonoOrTelefonoSecundario(telefono, telefonoSecundario);
        }

        if (existe) {
            throw new ElementoRepetidoException(MensajeError.TELEFONO_YA_EXISTENTE);
        }
    }




    @Override
    public Persona buscarPersonaPorEmail(String email) throws ElementoNoEncontradoException {

        return
                clienteRepo.findByUser_Email(email).map(cliente -> (Persona) cliente)
                .or(() -> personalBodegaRepo.findByUser_Email(email)).map(personalBodega -> (Persona) personalBodega)
                .or(() -> recursosHumanosRepo.findByUser_Email(email)).map(recursosHumanos -> (Persona) recursosHumanos)
                .orElseThrow(() -> new ElementoNoEncontradoException(MensajeError.PERSONA_NO_ENCONTRADO));
    }


    @Override
    public void guardarPersonaBD(Persona persona) {
        if (persona instanceof Cliente cliente) {
            clienteRepo.save(cliente);
        } else if (persona instanceof PersonalBodega personal) {
            personalBodegaRepo.save(personal);
        } else if (persona instanceof RecursosHumanos rh) {
            recursosHumanosRepo.save(rh);}
    }


}
