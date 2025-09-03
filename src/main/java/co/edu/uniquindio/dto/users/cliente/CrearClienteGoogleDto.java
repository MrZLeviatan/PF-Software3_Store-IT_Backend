package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CrearClienteGoogleDto(


        @NotBlank @Length(max = 100) String nombre,

        @NotBlank @Length(max = 15) String telefono,

        @NotBlank String codigoPais,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        String codigoPaisSecundario,

        @NotBlank @Email String email,

        @NotNull UbicacionDto ubicacion
) {
}
