package co.edu.uniquindio.dto.users.cliente;

import jakarta.validation.constraints.NotBlank;

public record VerificacionClienteDto(

        @NotBlank String email,
        String codigo

) {
}
