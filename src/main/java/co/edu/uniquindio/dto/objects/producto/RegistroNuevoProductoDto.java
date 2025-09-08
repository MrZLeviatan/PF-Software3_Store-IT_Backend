package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
  Este es un record de Java que sirve como un DTO (Data Transfer Object) para el registro de
  un nuevo producto en el sistema de bodegas.
 *
  Su propósito es encapsular toda la información necesaria para crear un nuevo producto,
  incluyendo sus características principales, la cantidad, una imagen, y los detalles del
  movimiento inicial de ingreso. Las anotaciones de validación aseguran que los datos
  sean correctos antes de ser utilizados.
 */
public record RegistroNuevoProductoDto(
        // Código único del producto. No puede estar en blanco.

        @NotBlank String codigoProducto,
        // Nombre del producto. No puede estar en blanco.
        @NotBlank String nombre,
        // Cantidad del producto. No puede ser nula.
        @NotNull Integer cantidad,
        // Archivo de imagen del producto, opcional.
        MultipartFile imagenProducto,
        // Descripción detallada del producto. No puede estar en blanco.
        @NotBlank String descripcion,
        // Tipo de producto. No puede ser nulo.
        @NotNull TipoProducto tipoProducto,
        // ID de la bodega donde se almacenará el producto.
        String idBodega,
        // Email del personal de bodega responsable del registro. No puede estar en blanco.
        @NotBlank String emailPersonalBodega,
        // Descripción opcional del movimiento de ingreso del producto.
        String descripcionMovimiento) {
}