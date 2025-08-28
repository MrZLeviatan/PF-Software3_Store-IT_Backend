package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.model.entities.users.Cliente;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "INACTIVA")
    @Mapping(target = "user.codigoRestablecimiento", ignore = true)
    Cliente toEntity(CrearClienteDto clienteDTO);


    ClienteDto toDto(Cliente cliente);


}
