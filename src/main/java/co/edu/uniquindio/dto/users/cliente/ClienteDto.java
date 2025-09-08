package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.UserDto;
import co.edu.uniquindio.model.enums.TipoCliente;

/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para representar la información
  de un cliente. Su propósito principal es transferir datos de cliente de forma segura
  y eficiente, exponiendo solo la información necesaria al cliente.
 *
  Utiliza un 'record', una característica de Java que simplifica la creación de clases
  inmutables de datos. Contiene campos como la información personal del cliente, sus datos
  de usuario, tipo de cliente, NIT y ubicación.
 */
public record ClienteDto(
        // El ID del cliente.
        Long id,
        // Nombre completo del cliente.
        String nombre,
        // Teléfono principal del cliente.
        String telefono,
        // Teléfono secundario del cliente.
        String telefonoSecundario,
        // Objeto DTO que contiene la información del usuario (email, estado de la cuenta, etc.).
        UserDto user,
        // Tipo de cliente, ya sea NATURAL o JURIDICO.
        TipoCliente tipoCliente,
        // Número de identificación tributaria, relevante para clientes jurídicos.
        String nit,
        // Objeto DTO que contiene la información de la ubicación del cliente.
        UbicacionDto ubicacion
) {
}