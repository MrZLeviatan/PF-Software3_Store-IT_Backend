package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;


public record RegistroNuevoProductoDto(

        @NotBlank String codigoProducto,
        @NotBlank String nombre,
        @NotNull Integer cantidad,
        MultipartFile imagenProducto,
        @NotBlank String descripcion,
        @NotNull TipoProducto tipoProducto,
        String idBodega,

        // Parte del movimiento Producto
        @NotBlank String emailPersonalBodega,
        String descripcionMovimiento

) {
}
