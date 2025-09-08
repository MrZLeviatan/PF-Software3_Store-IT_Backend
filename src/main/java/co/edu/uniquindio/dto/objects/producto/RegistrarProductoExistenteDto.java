package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**  Este es un record de Java que sirve como un DTO (Data Transfer Object) para el registro
  de una nueva entrada de un producto que ya existe en el sistema de bodegas.
 *
 Su propósito es encapsular la información necesaria para agregar más unidades de un producto
 ya registrado. Las anotaciones de validación garantizan que los datos, como el código
 del producto, la cantidad y el email del responsable, sean correctos antes de ser procesados.
 */
public record RegistrarProductoExistenteDto(
        // Código único del producto ya existente. No puede estar en blanco.
        @NotBlank String codigoProducto,
        // Cantidad de productos a ingresar. No puede estar en blanco.
        @NotNull Integer cantidad,
        // Tipo de producto.
        TipoProducto tipoProducto,

        // Email del personal de bodega responsable del registro. No puede estar en blanco.
        @NotBlank String emailPersonalBodega,
        // Descripción opcional del movimiento de ingreso.

        String descripcion
) {
}