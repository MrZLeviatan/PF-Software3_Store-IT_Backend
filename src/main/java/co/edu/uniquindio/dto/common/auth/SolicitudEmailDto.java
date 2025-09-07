package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SolicitudEmailDto(

        @NotNull @Email String email
) {
}
