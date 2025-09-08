package co.edu.uniquindio.controller.cliente;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.common.google.GoogleTokenRequest;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
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

/** Este es un controlador REST para el registro y la autenticación de clientes.
  Proporciona endpoints para que los clientes se registren en la aplicación,
  ya sea de forma tradicional o a través de Google. También incluye un endpoint
  para verificar la cuenta del cliente mediante un código.
 *
  La anotación @RestController indica que la clase es un controlador REST.
  La anotación @RequestMapping("/api/store-it") define la ruta base para todos los
  endpoints del controlador.
  La anotación @RequiredArgsConstructor de Lombok genera automáticamente un constructor
  con los argumentos finales, inyectando el servicio ClienteService.
 */
@RestController
@RequestMapping("/api/store-it")
@RequiredArgsConstructor
public class ClienteBannerController {

    private final ClienteService clienteService;
    // Endpoint para el registro de clientes con datos tradicionales (no Google).
    @PostMapping("/registro-clientes")
    public ResponseEntity<MensajeDto<String>> registrarCliente(
            @Valid @RequestBody CrearClienteDto crearClienteDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        clienteService.registrarCliente(crearClienteDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    // Endpoint para validar un token de Google.
    @PostMapping("/validate-google")
    public ResponseEntity<MensajeDto<GoogleUserResponse>> validarGoogle(@RequestBody GoogleTokenRequest googleTokenRequest)
            throws ElementoIncorrectoException {

        GoogleUserResponse googleUserResponse = clienteService.validarToken(googleTokenRequest.idToken());

        return ResponseEntity.status(200).body(new MensajeDto<>(false,googleUserResponse));
    }


    // Endpoint para el registro de clientes utilizando una cuenta de Google.
    @PostMapping("/registro-clientes-google")
    public ResponseEntity<MensajeDto<String>> registrarClienteGoogle(
            @Valid @RequestBody CrearClienteGoogleDto crearClienteGoogleDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoEliminadoException, ElementoNoValidoException {

        clienteService.registroClienteGoogle(crearClienteGoogleDto);

        return ResponseEntity.ok().body(new MensajeDto<>(false,"Registro logrado exitosamente."));
    }


    // Endpoint para verificar la cuenta de un cliente con un código.
    @PostMapping("/verificar-registro-clientes")
    public ResponseEntity<MensajeDto<String>> verificarRegistroCliente(
            @Valid @RequestBody VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoIncorrectoException {

        clienteService.verificacionCliente(verificacionCodigoDto);
        return ResponseEntity.ok(new MensajeDto<>(false ,"Cliente verificado con éxito."));
    }
}