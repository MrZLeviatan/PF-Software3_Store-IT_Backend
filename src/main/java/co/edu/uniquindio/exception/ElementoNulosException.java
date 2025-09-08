package co.edu.uniquindio.exception;

/**
  Excepción personalizada para manejar situaciones donde se intenta realizar una operación
  con un elemento que es nulo.

  Esta excepción es una subclase de Exception y se usa para indicar un error específico
  en la lógica de la aplicación, facilitando la identificación y el manejo de
  problemas relacionados con valores nulos.
 */
public class ElementoNulosException extends Exception {

    // Constructor de la excepción que recibe un mensaje detallado del error.
    public ElementoNulosException(String mensaje) {
        super(mensaje);
    }
}