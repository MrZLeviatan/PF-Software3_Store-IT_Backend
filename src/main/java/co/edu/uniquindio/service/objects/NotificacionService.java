package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.model.entities.users.PersonalBodega;

/** Servicio encargado de la gestión de notificaciones en Store-IT.
   Permite enviar mensajes relacionados con movimientos de productos
   y avisos personalizados al personal de bodega.
*/
public interface NotificacionService {

    // Envía una notificación general sobre un movimiento de producto
    void notificarMovimientoProducto(String mensaje);

    // Envía una notificación específica a un miembro del personal de bodega
    void notificarCambioMovimiento(PersonalBodega persona, String mensaje);

}
