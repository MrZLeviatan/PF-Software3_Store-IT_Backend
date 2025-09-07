package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.model.entities.users.Cliente;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "INACTIVA")
    @Mapping(target = "user.codigo", ignore = true)
    @Mapping(target = "user.registroGoogle", constant = "false")
    Cliente toEntity(CrearClienteDto clienteDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nit", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "tipoCliente", constant = "NATURAL")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.estadoCuenta", constant = "ACTIVO") // Para Google, activa de inmediato
    @Mapping(target = "user.codigo", ignore = true) // No usamos código para Google
    @Mapping(target = "user.registroGoogle", constant = "true") // Marcamos como Google
    Cliente toEntityGoogle(CrearClienteGoogleDto crearClienteGoogleDto);


    ClienteDto toDto(Cliente cliente);


}
