package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

/** Este es un repositorio de Spring Data JPA para la entidad MovimientosProducto.
  Proporciona métodos para interactuar con la base de datos,
  permitiendo realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) y
  consultas personalizadas relacionadas con los movimientos de productos.

  La interfaz extiende JpaRepository y JpaSpecificationExecutor,
  lo que habilita funcionalidades de paginación, ordenación y
  la creación de consultas dinámicas.
 */
public interface MovimientoProductoRepo extends JpaRepository<MovimientosProducto, Long>, JpaSpecificationExecutor<MovimientosProducto> {

    // Busca y retorna una lista de movimientos asociados a un producto específico.
    List<MovimientosProducto> findByProducto(Producto producto);

    // Busca y retorna una lista de movimientos autorizados por un miembro del personal de bodega.
    List<MovimientosProducto> findByPersonalAutorizacion(PersonalBodega personalBodega);

    // Busca y retorna una lista de movimientos donde el personal es el responsable.
    List<MovimientosProducto> findByPersonalResponsable(PersonalBodega personalAutorizacion);

    // Busca y retorna una lista de movimientos filtrados por un producto y un tipo de movimiento.
    List<MovimientosProducto> findByProductoAndTipoMovimiento(Producto producto, TipoMovimiento tipo);

    // Busca y retorna una lista de movimientos filtrados por un tipo de movimiento.
    List<MovimientosProducto> findByTipoMovimiento(TipoMovimiento tipoMovimiento);

    // Busca y retorna una lista de movimientos de un producto dentro de un rango de fechas.
    List<MovimientosProducto> findByProductoAndFechaMovimientoBetween(Producto producto, LocalDateTime inicio, LocalDateTime fin);
}

