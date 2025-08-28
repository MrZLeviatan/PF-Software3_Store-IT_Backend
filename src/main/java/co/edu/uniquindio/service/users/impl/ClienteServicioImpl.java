package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.mapper.users.ClienteMapper;
import co.edu.uniquindio.model.entities.users.Cliente;
import co.edu.uniquindio.repository.users.ClienteRepo;
import co.edu.uniquindio.service.users.ClienteServicio;
import co.edu.uniquindio.service.utils.PhoneService;
import co.edu.uniquindio.service.utils.ValidacionCuentasServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClienteServicioImpl implements ClienteServicio {

    private final ClienteRepo clienteRexpo;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidacionCuentasServicio validacionCuentasServicio;
    private final PhoneService phoneService;


    @Override
    public void registrarCliente(CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException {

        // 1. Validamos que el email y Teléfono no esté registrado
        validacionCuentasServicio.validarEmailNoRepetido(crearClienteDto.user().email());
        validacionCuentasServicio.validarTelefonoNoRepetido(crearClienteDto.telefono(),crearClienteDto.telefonoSecundario());

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

        // 6. Guardamos en la base de datos.
        clienteRexpo.save(cliente);


    }
}
