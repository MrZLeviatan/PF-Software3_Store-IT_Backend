package co.edu.uniquindio.repository.objects;

import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoProductoRepo extends JpaRepository<MovimientosProducto, Long>, JpaSpecificationExecutor<MovimientosProducto> {


    List<MovimientosProducto> findByProducto(Producto producto);

    List<MovimientosProducto> findByPersonalAutorizacion(PersonalBodega personalBodega);

    List<MovimientosProducto> findByPersonalResponsable(PersonalBodega personalAutorizacion);

    // Filtrar por producto y tipo de movimiento
    List<MovimientosProducto> findByProductoAndTipoMovimiento(Producto producto, TipoMovimiento tipo);

    List<MovimientosProducto> findByTipoMovimiento(TipoMovimiento tipoMovimiento);
    // Filtrar por fecha
    List<MovimientosProducto> findByProductoAndFechaMovimientoBetween(Producto producto, LocalDateTime inicio, LocalDateTime fin);



}

