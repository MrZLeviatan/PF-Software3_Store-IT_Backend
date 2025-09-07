package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.UserDto;
import co.edu.uniquindio.model.enums.TipoCliente;

public record ClienteDto(


        Long id,
        String nombre,
        String telefono,
        String telefonoSecundario,
        UserDto user,
        TipoCliente tipoCliente,
        String nit,
        UbicacionDto ubicacion


) {
}
