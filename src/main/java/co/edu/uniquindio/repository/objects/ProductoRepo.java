package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/** Este repositorio de Spring Data JPA maneja las operaciones de persistencia para la entidad Producto.
  Proporciona métodos para buscar productos en la base de datos,
  permitiendo consultar por su código y verificar su existencia.
 */
public interface ProductoRepo extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    // Busca y retorna un producto por su código, si existe.
    Optional<Producto> findByCodigoProducto(String codigoProducto);

    // Busca y retorna un producto por su código solo si está autorizado.
    Optional<Producto> findByCodigoProductoAndIsAutorizadoTrue(String codigoProducto);

    // Verifica si un producto con el código dado ya existe en la base de datos.
    boolean existsByCodigoProducto(String codigoProducto);
}
