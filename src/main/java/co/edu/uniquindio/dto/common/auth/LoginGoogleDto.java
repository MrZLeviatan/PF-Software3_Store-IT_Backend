package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginGoogleDto(

        @NotBlank String idToken
) {
}
