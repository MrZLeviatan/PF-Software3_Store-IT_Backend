package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;

import java.time.LocalDateTime;

/**Este es un record de Java que sirve como un DTO (Data Transfer Object) para representar
  la información de un producto.

 Su propósito es transferir de manera segura los datos de un producto, como su
  identificación, nombre, cantidad, imagen y estado, desde el backend hacia el frontend
  o a otros servicios. Este enfoque evita exponer la entidad completa del producto y
  solo muestra la información relevante.
 */
public record ProductoDto(
        // ID único del producto.
        Long id,
        // Código de referencia del producto.
        String codigoProducto,
        // Nombre del producto.
        String nombre,
        // Cantidad disponible del producto.
        Integer cantidad,
        // URL o nombre de la imagen del producto.
        String imagen,
        // Descripción detallada del producto.
        String descripcion,
        // Tipo de producto.
        TipoProducto tipoProducto,
        // Estado actual del producto (ej: EN_BODEGA, VENDIDO).
        EstadoProducto estadoProducto,
        // ID de la bodega donde se encuentra el producto.
        String idBodega
) {
}
