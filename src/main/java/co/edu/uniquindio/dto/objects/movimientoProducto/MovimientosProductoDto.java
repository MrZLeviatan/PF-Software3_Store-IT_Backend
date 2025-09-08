package co.edu.uniquindio.dto.objects.movimientoProducto;

import co.edu.uniquindio.model.enums.TipoMovimiento;
import java.time.LocalDateTime;

/**
 * Record MovimientosProductoDto
 * DTO (Data Transfer Object) que representa la información de un movimiento
 * dentro del sistema Store-IT.
 *
 * Los movimientos se relacionan con la gestión de bodegas:
 * entradas y salidas de productos, junto con su autorización y responsables.
 */
public record MovimientosProductoDto(

        /*
         * A continuación se presentan los atributos que almacenan la información principal de un movimiento
         * de productos en Store-IT: identificadores, descripciones, tipo de
         * movimiento, fechas relevantes y los datos del
         * personal involucrado (responsable y autorizado).
         */
        Long id,
        String descripcion,
        TipoMovimiento tipoMovimiento,
        LocalDateTime fechaMovimiento,
        LocalDateTime fechaAutorizacion,
        String descripcionAutorizado,
        String idProducto,
        String idPersonalResponsable,
        String idPersonalAutorizado

        ) {
}
