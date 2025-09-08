package co.edu.uniquindio.dto.common.user;

/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para encapsular
  la información de un código de seguridad.
 *
  Su propósito es transferir datos de códigos de verificación, como los utilizados
  para el restablecimiento de contraseñas. Incluye el código en sí, su fecha de expiración
  y el tipo de código, lo que facilita su validación y gestión en la lógica del negocio.
 */
import co.edu.uniquindio.model.enums.TipoCodigo;
import java.time.LocalDateTime;

public record CodigoDto(
        // El código de restablecimiento o verificación.
        String codigoRestablecimiento,
        // La fecha y hora en que el código dejará de ser válido.
        LocalDateTime fechaExpiracion,
        // El tipo de código, como por ejemplo, para restablecer contraseña.
        TipoCodigo tipoCodigo

) {
}