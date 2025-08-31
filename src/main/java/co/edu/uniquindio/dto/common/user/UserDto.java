package co.edu.uniquindio.dto.common.user;

import co.edu.uniquindio.model.enums.EstadoCuenta;
import jakarta.validation.constraints.Email;

public record UserDto(


    @Email String email,
    String password,
    EstadoCuenta estadoCuenta,
    CodigoDto codigoRestablecimiento


) {
}
