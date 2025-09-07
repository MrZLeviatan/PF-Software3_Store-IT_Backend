package co.edu.uniquindio.model.entities.objects;

import co.edu.uniquindio.model.embeddable.Ubicacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bodegas")
public class Bodega {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("ID único de la bodega.")
    private Long id;

    @Embedded
    private Ubicacion ubicacion;

    @Column(name = "direccion")
    @Comment("Dirección física de la sede")
    private String direccion;

    @Column(name = "telefono", nullable = false)
    @Comment("Número telefónico principal de la bodega.")
    private String telefono;

    @OneToMany(mappedBy = "producto", cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @Comment("Productos que se encuentran en esta bodega.")
    private List<Producto> productos;

}
