package co.edu.uniquindio.config;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.MailerRegularBuilder;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Clase de configuración que expone un bean Mailer (Simple Java Mail).
@Configuration
public class MailConfig {

    @Value("${smtp.host}")
    private String host;

    @Value("${smtp.port}")
    private int port;

    @Value("${smtp.user}")
    private String user;

    @Value("${smtp.password}")
    private String password;

    @Value("${smtp.transport:SMTP_TLS}")
    private String transport;

    @Value("${smtp.debug:false}")
    private boolean debug;

    @Value("${smtp.trustAllHosts:false}")
    private boolean trustAllHosts;

    @Value("${smtp.timeout.connect:10000}")
    private int connectTimeoutMs;

    @Value("${smtp.timeout.read:15000}")
    private int readTimeoutMs;


    /*
     Crea y devuelve el bean Mailer configurado con las propiedades leídas.
     Este bean se inyectará en los servicios que envíen correos.
    */
    @Bean
    public Mailer mailer() {
        // Construcción base del builder con servidor SMTP y credenciales
        MailerRegularBuilder builder =
                MailerBuilder.withSMTPServer(host, port, user, password)
                        .withTransportStrategy(mapTransport(transport))
                        .withDebugLogging(debug)
                        // Timeouts (robustez frente a conexiones lentas)
                        .withProperty("mail.smtp.connectiontimeout", String.valueOf(connectTimeoutMs))
                        .withProperty("mail.smtp.timeout", String.valueOf(readTimeoutMs))
                        .withProperty("mail.smtp.writetimeout", String.valueOf(readTimeoutMs));

        // Si estamos en un entorno con certificados problemáticos, confiar en todos (solo temporal)
        if (trustAllHosts) {
            builder.withProperty("mail.smtp.ssl.trust", "*");
        }

        // Construye y devuelve el Mailer listo para usar
        return builder.buildMailer();
    }

    /*
     Traduce la cadena de transporte a TransportStrategy de Simple Java Mail.
     Valores soportados: "SMTP", "SMTP_TLS", "SMTPS"
    */
    private TransportStrategy mapTransport(String t) {
        if (t == null) return TransportStrategy.SMTP_TLS;
        switch (t.trim().toUpperCase()) {
            case "SMTPS":
                return TransportStrategy.SMTPS;
            case "SMTP":
                return TransportStrategy.SMTP;
            case "SMTP_TLS":
            default:
                return TransportStrategy.SMTP_TLS;
        }
    }
}