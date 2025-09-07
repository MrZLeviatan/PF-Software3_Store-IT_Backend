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

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {


    private final NotificacionRepo notificacionRepo;
    private final PersonalBodegaRepo personalBodegaRepo;

    @Override
    public void notificarMovimientoProducto(String mensaje) {
        // Obtener todos los Admin y Gestores
        List<PersonalBodega> destinatarios = personalBodegaRepo.findByTipoPersonalBodegaIn(List.of(
                TipoPersonalBodega.ADMINISTRADOR_BODEGA, TipoPersonalBodega.GESTOR_BODEGA));

        for (PersonalBodega p : destinatarios) {
            Notificacion n = new Notificacion();
            n.setMensaje(mensaje);
            n.setDestinatario(p);
            notificacionRepo.save(n);
        }
    }


    @Override
    public void notificarCambioMovimiento(PersonalBodega persona, String mensaje) {
        Notificacion n = new Notificacion();
        n.setMensaje(mensaje);
        n.setDestinatario(persona);
        notificacionRepo.save(n);
    }

}

