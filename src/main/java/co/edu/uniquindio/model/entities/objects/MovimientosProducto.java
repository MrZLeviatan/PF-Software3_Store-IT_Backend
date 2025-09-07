package co.edu.uniquindio.model.entities.objects;

import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movimientos_producto")
public class MovimientosProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("ID único del movimiento.")
    private Long id;

    @Column(name = "descripción_movimiento")
    @Comment("Observaciones adicionales del movimiento")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento")
    @Comment("Tipo de movimiento: Ingreso, Retiro, Correctivo.")
    private TipoMovimiento tipoMovimiento;

    @Column(name = "fecha_movimiento", nullable = false)
    @Comment("Fecha y hora en que se realizó el movimiento")
    private LocalDateTime fechaMovimiento;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @Comment("Producto al que corresponde este movimiento.")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "personal_encargado_id", nullable = false)
    @Comment("Personal de bodega responsable del movimiento del producto.")
    private PersonalBodega personalResponsable;

    @ManyToOne
    @JoinColumn(name = "personal_autorización_id", nullable = false)
    @Comment("Personal de bodega responsable de la autorización del movimiento del producto.")
    private PersonalBodega personalAutorizacion;


}
