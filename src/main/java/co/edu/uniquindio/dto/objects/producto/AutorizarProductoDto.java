package co.edu.uniquindio.dto.objects.producto;

import jakarta.validation.constraints.NotBlank;

public record AutorizarProductoDto(


        @NotBlank String codigoProducto,

        // Parte del movimiento
        @NotBlank String emailPersonalAutorizado,
        String descripcionAutorizacion,
        Boolean autorizadoAutorizacion


) {
}
