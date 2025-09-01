package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.common.email.EmailDto;

public interface EmailService {


    void enviarEmailVerificacionRegistro(EmailDto emailDto);

    void enviarEmailRegistroGoogle (EmailDto emailDto);

    void enviarEmailVerificacionLogin(EmailDto emailDto);



}
