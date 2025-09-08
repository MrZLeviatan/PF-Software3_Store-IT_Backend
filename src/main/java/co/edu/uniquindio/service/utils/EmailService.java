package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.common.email.EmailDto;

/** Servicio para la gestión de envío de correos electrónicos en la aplicación.
   Incluye operaciones relacionadas con la verificación de registro,
   integración con Google y envío de códigos a través de plantillas HTML. */
public interface EmailService {

    /** Envía un correo de verificación de registro a la dirección proporcionada
       en el objeto EmailDto. */
    void enviarEmailVerificacionRegistro(EmailDto emailDto);

    /** Envía un correo de confirmación para usuarios registrados mediante Google. */
    void enviarEmailRegistroGoogle(EmailDto emailDto);

    /** Envía un correo con un código de validación, utilizando la plantilla HTML
       ubicada en la ruta especificada. */
    void enviarEmailCodigo(EmailDto emailDto, String rutaHtml);

}
