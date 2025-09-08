package co.edu.uniquindio.exception;

/** Excepción personalizada para indicar que se intenta realizar una operación sobre
  un elemento que ya ha sido eliminado.

  Esta clase extiende de la clase Exception y se utiliza para manejar errores específicos
  en la lógica de la aplicación, como prevenir que se actualice o se consulte un objeto
  que ya no existe en la base de datos de manera lógica o física.
 */
public class ElementoEliminadoException extends Exception {
    // Constructor que recibe un mensaje detallado para describir el error.
    public ElementoEliminadoException(String message) {
        super(message);
    }
}