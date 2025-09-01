package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.model.enums.TipoCliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CrearClienteDto(


        @NotBlank @Length(max = 100) String nombre,

        @NotBlank @Length(max = 15) String telefono,

        @NotBlank String codigoPais,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        String codigoPaisSecundario,

        // Información del usuario (correo y contraseña).
        @NotNull @Valid CrearUserDto user,

        // Información de la ubicación del cliente.
        @NotNull @Valid UbicacionDto ubicacion,

        // Tipo de cliente.
        @NotNull TipoCliente tipoCliente,
        
        String nit

) {
}
