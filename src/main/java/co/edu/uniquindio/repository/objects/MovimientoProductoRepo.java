package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovimientoProductoRepo extends JpaRepository<MovimientosProducto, Long>, JpaSpecificationExecutor<MovimientosProducto> {
}
