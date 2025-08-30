package co.edu.uniquindio.controller.cliente;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.VerificacionClienteDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.service.users.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clienteBanner") // Ubicado aquí si el registro es parte del flujo de autenticación
@RequiredArgsConstructor
public class ClienteBannerController {

    private final ClienteService clienteService;



    @PostMapping("/registro")
    public ResponseEntity<MensajeDto<String>> registrarCliente(
            @Valid @RequestBody CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException {

        clienteService.registrarCliente(crearClienteDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }

    @PostMapping("/verificar-registro")
    public ResponseEntity<MensajeDto<String>> verificarRegistroCliente(
            @Valid @RequestBody VerificacionClienteDto verificacionClienteDto)
            throws ElementoNoEncontradoException {

        clienteService.verificacionCliente(verificacionClienteDto);
        return ResponseEntity.ok(new MensajeDto<>(false ,"Cliente verificado con éxito."));
    }


}
