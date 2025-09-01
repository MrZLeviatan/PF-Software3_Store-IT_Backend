package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarPasswordDto (

    @NotBlank @NotNull String email,
    @NotBlank @Size(min = 8) String nuevaPassword

){
}
