package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {


}
