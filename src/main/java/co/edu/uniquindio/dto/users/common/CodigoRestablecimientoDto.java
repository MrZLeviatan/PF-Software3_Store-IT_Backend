package co.edu.uniquindio.dto.users.common;

import co.edu.uniquindio.model.enums.TipoCodigo;
import java.time.LocalDateTime;

public record CodigoRestablecimientoDto(


        String codigoRestablecimiento,
        LocalDateTime fechaExpiracion,
        TipoCodigo tipoCodigo

) {
}
