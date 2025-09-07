package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.Notificacion;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepo extends JpaRepository<Notificacion, Long> {


    List<Notificacion> findByDestinatarioAndLeidaFalse(PersonalBodega destinatario);
}

