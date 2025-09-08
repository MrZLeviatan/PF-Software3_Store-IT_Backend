package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.cliente.ClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteDto;
import co.edu.uniquindio.dto.users.cliente.CrearClienteGoogleDto;
import co.edu.uniquindio.model.entities.users.Cliente;
import org.mapstruct.*;

/** Este es un Mapper de MapStruct para la entidad Cliente.
  Su propósito es convertir entre objetos DTO (Data Transfer Object) y la entidad Cliente.
  Se encarga de mapear datos para la creación de clientes, tanto de forma regular como a través de Google.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper {

    // Convierte un DTO (CrearClienteDto) a una entidad (Cliente) para un registro normal.
    // Ignora el ID y el código, establece el estado de la cuenta como INACTIVA y marca el registro como no de Google.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "user.estadoCuenta", constant = "INACTIVA")
    @Mapping(target = "user.codigo", ignore = true)
    @Mapping(target = "user.registroGoogle", constant = "false")
    Cliente toEntity(CrearClienteDto clienteDTO);

    // Convierte un DTO (CrearClienteGoogleDto) a una entidad (Cliente) para un registro con Google.
    // Ignora el ID y el NIT, establece el estado de la cuenta como ACTIVA y marca el registro como de Google.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nit", ignore = true)
    @Mapping(target = "ubicacion", source = "ubicacion")
    @Mapping(target = "tipoCliente", constant = "NATURAL")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.estadoCuenta", constant = "ACTIVO")
    @Mapping(target = "user.codigo", ignore = true)
    @Mapping(target = "user.registroGoogle", constant = "true")
    Cliente toEntityGoogle(CrearClienteGoogleDto crearClienteGoogleDto);

    // Convierte una entidad (Cliente) a un DTO (ClienteDto) para su visualización.
    ClienteDto toDto(Cliente cliente);
}