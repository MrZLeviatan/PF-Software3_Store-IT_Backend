package co.edu.uniquindio.dto.common.user;

import co.edu.uniquindio.model.enums.TipoCodigo;
import java.time.LocalDateTime;

public record CodigoDto(

        String codigoRestablecimiento,
        LocalDateTime fechaExpiracion,
        TipoCodigo tipoCodigo

) {
}
