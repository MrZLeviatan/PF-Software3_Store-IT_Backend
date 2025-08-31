package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.dto.common.email.EmailDto;
import co.edu.uniquindio.service.utils.EmailService;
import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.simplejavamail.api.email.Email;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final Mailer mailer;

    @Value("${smtp.from.address}")
    private String fromAddress;

    @Value("${smtp.from.name}")
    private String fromName;

    @Value("${app.verification.base-url}")
    private String verificationBaseUrl;


    @Override
    public void enviarEmailVerificacion(EmailDto emailDto) {
        try {
            // 1. Cargar plantilla HTML
            String htmlTemplate = loadHtmlTemplate("templates/verificacion.html");

            // 2. Se agrega la url de verificación
            String verificationUrl = verificationBaseUrl
                    + "?email=" + URLEncoder.encode(emailDto.destinatario(), StandardCharsets.UTF_8)
                    + "&codigo=" + URLEncoder.encode(emailDto.cuerpo(), StandardCharsets.UTF_8);

            // 3. Reemplazar variables dinámicas
            String cuerpoPersonalizado = htmlTemplate
                    .replace("{{codigo}}", emailDto.cuerpo())
                    .replace("{{verification_link}}", verificationUrl);


            // 4. Construir el correo con Simple Java Mail
            Email email = EmailBuilder.startingBlank()
                    .from(fromName, fromAddress)
                    .to(emailDto.destinatario())
                    .withSubject(emailDto.asunto())
                    .withHTMLText(cuerpoPersonalizado)
                    .buildEmail();

            // 5. Enviar el correo
            mailer.sendMail(email);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }


    @Override
    public void enviarEmailRegistroGoogle(EmailDto emailDto) {
        try {
            // 1. Cargar plantilla HTML
            String htmlTemplate = loadHtmlTemplate("templates/registroClienteGoogle.html");

            // 2. Construir el correo con Simple Java Mail
            Email email = EmailBuilder.startingBlank()
                    .from(fromName, fromAddress)
                    .to(emailDto.destinatario())
                    .withSubject(emailDto.asunto())
                    .withHTMLText(htmlTemplate)
                    .buildEmail();

            // 3. Enviar el correo
            mailer.sendMail(email);

        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la plantilla del correo", e);
        }
    }


    // Método auxiliar para leer un archivo HTML desde resources
    private String loadHtmlTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        byte[] bytes = resource.getInputStream().readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }


}
