package co.edu.uniquindio.exception;

/** Excepción personalizada para indicar que un elemento o valor no coincide con
  lo esperado en la lógica de la aplicación.
 *
  Esta clase se utiliza para manejar errores específicos donde la identidad,
  el tipo o el valor de un objeto no corresponde a los requisitos de una operación.
  Por ejemplo, al intentar asociar un producto con una bodega que no le corresponde.
 */
public class ElementoNoCoincideException extends Exception {
    // Constructor que recibe un mensaje para describir el error de no coincidencia.
    public ElementoNoCoincideException(String message) {
        super(message);
    }
}