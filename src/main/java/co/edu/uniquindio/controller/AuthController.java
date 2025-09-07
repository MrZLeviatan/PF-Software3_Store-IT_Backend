package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.*;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.exception.*;
import co.edu.uniquindio.service.common.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Ubicado aquí si el registro es parte del flujo de autenticación
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;


    @PostMapping("/login")
    private ResponseEntity<MensajeDto<String>> login (@Valid @RequestBody LoginDto loginDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException, ElementoNoCoincideException {

        authService.login(loginDto);

        // Retorna una respuesta HTTP 200 con el token dentro de un objeto de tipo MensajeDto
        return ResponseEntity.status(200).body(new MensajeDto<>(false,"En espera de verificación login"));
    }


    @PostMapping("/login-google")
    private ResponseEntity<MensajeDto<GoogleUserResponse>> loginGoogle (@Valid @RequestBody LoginGoogleDto loginGoogleDto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException {

        GoogleUserResponse googleUserResponse = authService.loginGoogle(loginGoogleDto);

        // Retorna una respuesta HTTP 200 con el token dentro de un objeto de tipo MensajeDto
        return ResponseEntity.status(200).body(new MensajeDto<>(false,googleUserResponse));
    }


    @PostMapping("/login-verificación")
    private ResponseEntity<MensajeDto<TokenDto>> verififcarLogin(@Valid @RequestBody VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException {

        TokenDto tokenDto = authService.verificacionLogin(verificacionCodigoDto);

        return ResponseEntity.status(200).body(new MensajeDto<>(false,tokenDto));

    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<MensajeDto<String>> solicitarRestablecimientoPassword(@RequestBody @Valid SolicitudEmailDto dto)
            throws ElementoNoEncontradoException, ElementoEliminadoException, ElementoNoValidoException {
        authService.solicitarRestablecimientoPassword(dto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Solicitud de restablecimiento de contraseña enviada"));
    }


    @PostMapping("/verificar-codigo-restablecimiento-password")
    public ResponseEntity<MensajeDto<String>> verificarCodigoPassword(@RequestBody @Valid VerificacionCodigoDto dto)
            throws ElementoNoEncontradoException, ElementoNoValidoException, ElementoNoCoincideException {

        authService.verificarCodigoPassword(dto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Código verificado exitosamente"));
    }


    @PutMapping("/actualizar-password")
    public ResponseEntity<MensajeDto<String>> actualizarPassword(@RequestBody @Valid ActualizarPasswordDto dto)
            throws ElementoNoEncontradoException {

        authService.actualizarPassword(dto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Contraseña actualizada correctamente"));
    }


}
