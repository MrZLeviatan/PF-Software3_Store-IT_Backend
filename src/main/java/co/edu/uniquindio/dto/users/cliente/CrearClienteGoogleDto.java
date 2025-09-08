package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 Este es un record de Java que funciona como un DTO (Data Transfer Object) para la creación
 de un nuevo cliente que se registra utilizando una cuenta de Google.
 *
  Utiliza anotaciones de validación de Jakarta para asegurar que los datos
  proporcionados cumplan con los requisitos antes de ser procesados.
  Esto ayuda a mantener la integridad de la información y a prevenir errores.
 */
public record CrearClienteGoogleDto(
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

        // Email del cliente. No puede estar en blanco y debe ser un formato de email válido.
        @NotBlank @Email String email,

        // Contraseña del cliente. No puede estar en blanco.
        @NotBlank String password,

        // Ubicación del cliente. No puede ser nula.
        @NotNull UbicacionDto ubicacion
) {
}