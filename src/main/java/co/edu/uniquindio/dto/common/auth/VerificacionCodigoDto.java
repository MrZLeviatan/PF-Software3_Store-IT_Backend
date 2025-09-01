package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerificacionCodigoDto(

        @NotBlank @Email String email,
        String codigo

) {
}
