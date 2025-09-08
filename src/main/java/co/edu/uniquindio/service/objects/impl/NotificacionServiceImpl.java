package co.edu.uniquindio.service.objects.impl;

import co.edu.uniquindio.model.entities.objects.Notificacion;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoPersonalBodega;
import co.edu.uniquindio.repository.objects.NotificacionRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.service.objects.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/** Implementación del servicio de notificaciones en Store-IT.
   Se encarga de registrar y enviar notificaciones a los usuarios del sistema
   cuando se producen cambios o movimientos de productos en bodega.
 */
@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    // Repositorio para guardar notificaciones en la base de datos
    private final NotificacionRepo notificacionRepo;

    // Repositorio para acceder al personal de bodega
    private final PersonalBodegaRepo personalBodegaRepo;

    /** Envía una notificación a todos los administradores y gestores de bodega
       cuando ocurre un movimiento de producto.
     */
    @Override
    public void notificarMovimientoProducto(String mensaje) {
        // Obtener todos los destinatarios con rol ADMINISTRADOR o GESTOR
        List<PersonalBodega> destinatarios = personalBodegaRepo.findByTipoPersonalBodegaIn(List.of(
                TipoPersonalBodega.ADMINISTRADOR_BODEGA, TipoPersonalBodega.GESTOR_BODEGA));

        // Crear y guardar una notificación para cada destinatario
        for (PersonalBodega p : destinatarios) {
            Notificacion n = new Notificacion();
            n.setMensaje(mensaje);
            n.setDestinatario(p);
            notificacionRepo.save(n);
        }
    }

    /** Envía una notificación personalizada a un auxiliar o responsable
       cuando su movimiento ha sido autorizado o denegado.
     */
    @Override
    public void notificarCambioMovimiento(PersonalBodega persona, String mensaje) {
        Notificacion n = new Notificacion();
        n.setMensaje(mensaje);
        n.setDestinatario(persona);
        notificacionRepo.save(n);
    }
}
