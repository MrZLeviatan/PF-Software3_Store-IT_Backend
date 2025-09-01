package co.edu.uniquindio.controller;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.TokenDto;
import co.edu.uniquindio.dto.common.auth.ActualizarPasswordDto;
import co.edu.uniquindio.dto.common.auth.LoginDto;
import co.edu.uniquindio.dto.common.auth.SolicitudEmailDto;
import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
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
            throws ElementoRepetidoException, ElementoIncorrectoException, ElementoNoEncontradoException {

        authService.login(loginDto);

        // Retorna una respuesta HTTP 200 con el token dentro de un objeto de tipo MensajeDto
        return ResponseEntity.status(200).body(new MensajeDto<>(false,"En espera de verificación login"));
    }

    @PostMapping("/login-verificación")
    private ResponseEntity<MensajeDto<TokenDto>> verififcarLogin(@Valid @RequestBody VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException {

        TokenDto tokenDto = authService.verificacionLogin(verificacionCodigoDto);

        return ResponseEntity.status(200).body(new MensajeDto<>(false,tokenDto));

    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<MensajeDto<String>> solicitarRestablecimientoPassword(@RequestBody @Valid SolicitudEmailDto dto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException {
        authService.solicitarRestablecimientoPassword(dto);
        return ResponseEntity.ok().body(new MensajeDto<>(false,"Solicitud de restablecimiento de contraseña enviada"));
    }


    @PostMapping("/verificar-codigo-restablecimiento-password")
    public ResponseEntity<MensajeDto<String>> verificarCodigoPassword(@RequestBody @Valid VerificacionCodigoDto dto)
            throws ElementoIncorrectoException, ElementoNoEncontradoException {

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
