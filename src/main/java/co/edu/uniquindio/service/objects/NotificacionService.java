package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.model.entities.users.PersonalBodega;

public interface NotificacionService {

    void notificarMovimientoProducto(String mensaje);

    void notificarCambioMovimiento(PersonalBodega persona, String mensaje);

}
