package co.edu.uniquindio.service.utils.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {


    @Value("${smtp.host}")
    private String host;  // Dirección del servidor SMTP

    @Value("${smtp.port}")
    private String port; // Puerto del servidor SMTP

    @Value("${smtp.user}")
    private String user; // Correo electrónico del remitente (usado como autenticación)

    @Value("${smtp.password}")
    private String password;  // Contraseña del remitente


}
