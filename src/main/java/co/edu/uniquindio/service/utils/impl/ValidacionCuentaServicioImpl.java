package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.ElementoEliminadoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.model.entities.users.Persona;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.utils.ValidacionCuentasServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ValidacionCuentaServicioImpl implements ValidacionCuentasServicio {


    private final ClienteRepo clienteRepo;


    @Override
    public void validarEmailNoRepetido(String email) throws ElementoRepetidoException, ElementoEliminadoException {
        // Se busca en el repositorio de cada tipo de usuario (Cliente, Agente de Ventas, Personal de Bodega, Recursos Humanos)
        // Si el email existe en alguno de los repositorios, se retorna la persona encontrada.
        Optional<? extends Persona> persona =
                clienteRepo.findByUser_Email(email);

        if (persona.isPresent()) {
            // Verifica si la cuenta no esta eliminada.
            if (persona.get().getUser().getEstadoCuenta().equals(EstadoCuenta.ELIMINADO)) {
                throw new ElementoEliminadoException(MensajeError.EMAIL_CUENTA_ELIMINADA);
            }
            throw new ElementoRepetidoException(MensajeError.EMAIL_YA_EXISTE);
        }}


    @Override
    public void validarTelefonoNoRepetido(String telefono, String telefonoSecundario) throws ElementoRepetidoException {

        // Buscar si existe en teléfono principal o secundario
        boolean existe = clienteRepo.existsByTelefonoOrTelefonoSecundario(telefono, telefonoSecundario);

        if (existe) {
            throw new ElementoRepetidoException(MensajeError.TELEFONO_YA_EXISTENTE);
        }}


}
