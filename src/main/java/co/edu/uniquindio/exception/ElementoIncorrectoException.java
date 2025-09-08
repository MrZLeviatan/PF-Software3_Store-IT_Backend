package co.edu.uniquindio.exception;

/** Excepción personalizada para indicar que un elemento o valor no es del tipo o formato
  correcto para una operación.

  Esta excepción extiende de la clase Exception y se usa para manejar situaciones donde,
  por ejemplo, se espera un tipo de dato específico y se recibe otro, o cuando un objeto
  no cumple con las condiciones necesarias para ser procesado.
 */
public class ElementoIncorrectoException extends Exception{
    // Constructor que recibe un mensaje detallado para describir la incorrección del elemento.
    public ElementoIncorrectoException(String mensaje) {

        super(mensaje);
    }
}
