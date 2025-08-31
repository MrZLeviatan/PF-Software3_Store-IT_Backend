package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.common.email.EmailDto;

public interface EmailService {


    void enviarEmailVerificacion (EmailDto emailDto);

    void enviarEmailRegistroGoogle (EmailDto emailDto);



}
