package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.exception.ElementoEliminadoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.embeddable.Codigo;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoCodigo;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.utils.EmailService;
import co.edu.uniquindio.service.utils.ValidacionCuentasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ValidacionCuentaServiceImpl implements ValidacionCuentasService {

    private final ClienteRepo clienteRepo;
    private final EmailService emailService;



    @Override
    public void validarEmailNoRepetido(String email)
            throws ElementoRepetidoException, ElementoEliminadoException {

        //  Se busca el email en el repositorio de clientes
        Optional<? extends Persona> personaOpt = clienteRepo.findByUser_Email(email);

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
                emailService.enviarEmailVerificacion(emailDto);

                throw new ElementoRepetidoException(
                        "La cuenta ya existe pero está inactiva. "
                                + "Hemos enviado un nuevo código de verificación a tu correo."
                );
            }
        }
    }


    @Override
    public void validarTelefonoNoRepetido(String telefono, String telefonoSecundario) throws ElementoRepetidoException {

        // Buscar si existe en teléfono principal o secundario
        boolean existe = clienteRepo.existsByTelefonoOrTelefonoSecundario(telefono, telefonoSecundario);

        if (existe) {
            throw new ElementoRepetidoException(MensajeError.TELEFONO_YA_EXISTENTE);
        }}


}
