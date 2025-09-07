package co.edu.uniquindio.model.entities.objects;

import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("ID único del producto")
    private Long id;

    @Column(name = "nombre", nullable = false)
    @Comment("Nombre del producto")
    private String nombre;

    @Column(name = "imagen_producto_url")
    @Comment("URL de la imagen del producto (Almacenada en Cloudinary).")
    private String imagen;

    @Column(name = "descripcion")
    @Comment("Descripción detallada del producto")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_producto", nullable = false)
    @Comment("Tipo de producto: FRÁGIL, PERECEDERO. ESTANDAR")
    private TipoProducto tipoProducto;

    @Column(name = "fecha_vencimiento", nullable = true)
    @Comment("Fecha de vencimiento del producto ( Si aplica )")
    private LocalDateTime fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_producto", nullable = false)
    @Comment("Estado del producto: en bodega, trasladado o retirado")
    private EstadoProducto estadoProducto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bodega_id", nullable = false)
    @Comment("Bodega en la que se encuentra el productol.")
    private Bodega bodega;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @Comment("Historial de movimientos del producto.")
    private List<MovimientosProducto> historialMovimientos;


}
