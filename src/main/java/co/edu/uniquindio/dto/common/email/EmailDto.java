package co.edu.uniquindio.dto.common.email;

/** Este es un record de Java que sirve como un DTO (Data Transfer Object) para encapsular
  la información necesaria para enviar un correo electrónico.
 *
  Su propósito es transferir de manera estructurada los detalles de un email,
  como el destinatario, el cuerpo del mensaje y el asunto. Este DTO simplifica
  la comunicación con servicios de envío de correos, asegurando que todos los
  datos requeridos estén presentes.
 */
public record EmailDto(
        // La dirección de correo electrónico del destinatario.
        String destinatario,
        // El contenido principal del email. Puede ser un mensaje de texto o un código de verificación.
        String cuerpo,
        // El asunto o título del correo electrónico.
        String asunto
) {
}