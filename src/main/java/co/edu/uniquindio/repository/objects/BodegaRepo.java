package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BodegaRepo extends JpaRepository<Bodega, Long>, JpaSpecificationExecutor<Bodega> {

    // Busca una bodega por su id (sobrescribe el método findById estándar)
    Bodega findById(long id);
}
