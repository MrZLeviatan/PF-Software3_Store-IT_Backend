package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.Notificacion;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Este repositorio maneja las operaciones de persistencia para la entidad Notificacion.
  Proporciona métodos para consultar notificaciones en la base de datos.
 */

public interface NotificacionRepo extends JpaRepository<Notificacion, Long> {
    // Busca y retorna una lista de notificaciones no leídas para un destinatario específico.
    List<Notificacion> findByDestinatarioAndLeidaFalse(PersonalBodega destinatario);
}