package co.edu.uniquindio.exception;


import co.edu.uniquindio.dto.MensajeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ⚠️ Elemento repetido (400 - Bad Request)
    @ExceptionHandler(ElementoRepetidoException.class)
    public ResponseEntity<MensajeDto<String>> manejarElementoRepetido(ElementoRepetidoException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MensajeDto<>(true, e.getMessage()));
    }

    // ⚠️ Elemento repetido (400 - Bad Request)
    @ExceptionHandler(ElementoNulosException.class)
    public ResponseEntity<MensajeDto<String>> manejarElementoRepetido(ElementoNulosException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MensajeDto<>(true, e.getMessage()));
    }

    // ⚠️ Elemento no encontrado (404 - Not Found)
    @ExceptionHandler(ElementoNoEncontradoException.class)
    public ResponseEntity<MensajeDto<String>> manejarElementoNoEncontrado(ElementoNoEncontradoException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new MensajeDto<>(true, e.getMessage()));
    }


    // ⚠️ Elemento eliminado (410 - GONE)
    @ExceptionHandler(ElementoEliminadoException.class)
    public ResponseEntity<MensajeDto<String>> manejarEliminado(ElementoEliminadoException e) {
        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(new MensajeDto<>(true, e.getMessage()));
    }



    // ⚠️ Errores de validación de campos (@Valid) (400 - Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensajeDto<List<String>>> manejarValidaciones(MethodArgumentNotValidException e) {
        List<String> errores = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MensajeDto<>(true, errores));
    }





    // ⚠️ Cualquier otro error inesperado (500 - Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeDto<String>> manejarErroresGenerales(Exception e) {
        e.printStackTrace(); // Para depuración en consola
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeDto<>(true, "Error inesperado: " + e.getMessage()));
    }
}
