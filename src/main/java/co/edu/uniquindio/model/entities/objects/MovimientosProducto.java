package co.edu.uniquindio.model.entities.objects;

import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.EstadoMovimiento;
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

    @Column(name = "fecha_autorización")
    @Comment("Fecha y hora en que se autoriza el movimiento.")
    private LocalDateTime fechaAutorizacion;

    @Column(name = "esta_verificado", nullable = false)
    @Comment("Indica si el movimiento esta en revision o no.")
    private boolean isVerificado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_movimiento")
    @Comment("Estado del movimiento: Denegado, Verificado")
    private EstadoMovimiento estadoMovimiento;

    @Column(name = "descripción_autorización")
    @Comment("Observaciones adicionales de la autorización")
    private String descripcionAutorizado;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @Comment("Producto al que corresponde este movimiento.")
    private Producto producto;

    @Column(name = "cantidad_producto", nullable = true)
    @Comment("Cantidad a retirar o agregar del producto")
    private Integer cantidad;


    @ManyToOne
    @JoinColumn(name = "personal_encargado_id", nullable = false)
    @Comment("Personal de bodega responsable del movimiento del producto.")
    private PersonalBodega personalResponsable;

    @ManyToOne
    @JoinColumn(name = "personal_autorización_id")
    @Comment("Personal de bodega responsable de la autorización del movimiento del producto.")
    private PersonalBodega personalAutorizacion;


}
