package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.*;

public interface ClienteService {


    void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException;

    void verificacionCliente(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoValido;


    void registroClienteGoogle(CrearClienteGoogleDto crearClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException;

}
