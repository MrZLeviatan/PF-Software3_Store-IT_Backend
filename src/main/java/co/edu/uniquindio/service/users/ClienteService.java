package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;

public interface ClienteService {


    void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException;

    void verificacionCliente(VerificacionCodigoDto verificacionCodigoDto) throws ElementoNoEncontradoException;

    void registroClienteGoogle(CrearClienteGoogleDto crearClienteGoogleDto) throws ElementoRepetidoException, ElementoNulosException;

}
