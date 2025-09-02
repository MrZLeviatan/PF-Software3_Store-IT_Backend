package co.edu.uniquindio.controller.cliente;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.service.users.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store-it") // Ubicado aquí si el registro es parte del flujo de autenticación
@RequiredArgsConstructor
public class ClienteBannerController {

    private final ClienteService clienteService;


    @PostMapping("/registro-clientes")
    public ResponseEntity<MensajeDto<String>> registrarCliente(
            @Valid @RequestBody CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        clienteService.registrarCliente(crearClienteDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    @PostMapping("/registro-clientes-google")
    public ResponseEntity<MensajeDto<String>> registrarClienteGoogle(
            @Valid @RequestBody CrearClienteGoogleDto crearClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        clienteService.registroClienteGoogle(crearClienteGoogleDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }



    @PostMapping("/verificar-registro-clientes")
    public ResponseEntity<MensajeDto<String>> verificarRegistroCliente(
            @Valid @RequestBody VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoIncorrectoException {

        clienteService.verificacionCliente(verificacionCodigoDto);
        return ResponseEntity.ok(new MensajeDto<>(false ,"Cliente verificado con éxito."));
    }


}
