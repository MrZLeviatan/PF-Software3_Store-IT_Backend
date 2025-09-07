package co.edu.uniquindio.dto.objects.producto;

import co.edu.uniquindio.model.enums.EstadoMovimiento;
import jakarta.validation.constraints.NotBlank;

public record AutorizacionProductoDto(


        @NotBlank String codigoProducto,

        // Parte del movimiento
        @NotBlank String idMovimiento,
        @NotBlank String emailPersonalAutorizado,
        String descripcionAutorizacion,
        EstadoMovimiento estadoMovimiento


) {
}
