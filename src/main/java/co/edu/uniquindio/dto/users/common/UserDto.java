package co.edu.uniquindio.dto.users.common;

import co.edu.uniquindio.model.enums.EstadoCuenta;
import jakarta.validation.constraints.Email;

public record UserDto(


    @Email String email,
    EstadoCuenta estadoCuenta,


) {
}
