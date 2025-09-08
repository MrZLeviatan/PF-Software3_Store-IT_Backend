package co.edu.uniquindio.dto;

/**
  Este es un record de Java utilizado como un DTO (Data Transfer Object) para encapsular
  mensajes de respuesta.
 *
  Los records, introducidos en Java 16, son clases inmutables que actúan como
  portadores de datos, simplificando la creación de DTOs.
 *
  Este record tiene dos campos: 'error', un booleano que indica si la respuesta es
  un error, y 'mensaje', un campo genérico que puede contener cualquier tipo de dato,
  como un String o un objeto.
 */
public record MensajeDto<T>(boolean error, T mensaje) {

}
