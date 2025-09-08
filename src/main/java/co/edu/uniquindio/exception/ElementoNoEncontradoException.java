package co.edu.uniquindio.exception;

/**
  Excepción personalizada para indicar que un elemento o recurso específico
  no fue encontrado en la aplicación.

  Esta excepción extiende de la clase Exception y se utiliza para un manejo
  de errores más preciso, facilitando la identificación de situaciones
  en las que se busca un objeto que no existe en la base de datos o en una colección.
 */
public class ElementoNoEncontradoException extends Exception {
    // Constructor que recibe un mensaje detallado para describir el error.
    public ElementoNoEncontradoException(String message) {
        super(message);
    }
}