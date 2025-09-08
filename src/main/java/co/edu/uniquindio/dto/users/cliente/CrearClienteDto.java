package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.model.enums.TipoCliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para la creación
  de un nuevo cliente a través de un registro regular, sin usar una cuenta de Google.
 *
  Utiliza anotaciones de validación de Jakarta para garantizar que los datos
  recibidos sean válidos antes de su procesamiento.
  Este DTO incluye información detallada sobre el cliente, sus datos de usuario
  (como el email y la contraseña) y su ubicación.
 */
public record CrearClienteDto(
        // Nombre del cliente. No puede estar en blanco y tiene un máximo de 100 caracteres.
        @NotBlank @Length(max = 100) String nombre,

        // Teléfono principal. No puede estar en blanco y tiene un máximo de 15 caracteres.
        @NotBlank @Length(max = 15) String telefono,

        // Código de país del teléfono principal. No puede estar en blanco.
        @NotBlank String codigoPais,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        // Código de país del teléfono secundario.
        String codigoPaisSecundario,

        // Información del usuario (correo y contraseña). Debe ser válido y no nulo.
        @NotNull @Valid CrearUserDto user,

        // Información de la ubicación del cliente. Debe ser válida y no nula.
        @NotNull @Valid UbicacionDto ubicacion,

        // Tipo de cliente (Ej: NATURAL o JURIDICO). No puede ser nulo.
        @NotNull TipoCliente tipoCliente,

        // Número de identificación tributaria (NIT) del cliente, opcional.
        String nit
) {
}
