package co.edu.uniquindio.exception;

/**
  Excepción de tiempo de ejecución (RuntimeException) para indicar que se intenta
  eliminar un elemento que aún está en uso por otra parte del sistema,
  lo que podría causar inconsistencias de datos.
 *
  A diferencia de las excepciones verificadas (checked exceptions) que requieren
  ser declaradas en la firma del método, esta excepción no necesita ser manejada
  explícitamente, lo que es útil para errores que se consideran fallos de lógica
  de programación y no condiciones esperadas del negocio.
 */
public class ElementoAunEnUsoException extends RuntimeException {
    // Constructor que recibe un mensaje detallado para describir el error.
    public ElementoAunEnUsoException(String message) {
        super(message);
    }
}
