package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.enums.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {


    Optional<Producto> findByCodigoProducto(String codigoProducto);

    Optional<Producto> findByNombreAndTipoProducto(String nombre, TipoProducto tipoProducto);




}
