package co.edu.uniquindio.model.entities.users;


import co.edu.uniquindio.model.embeddable.DatosLaborales;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.enums.TipoPersonalBodega;
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
@Table(name = "personal_bodega")
public class PersonalBodega extends Persona{


    @Embedded
    private DatosLaborales datosLaborales;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_personal_bodega", nullable = false)
    @Comment("Tipo de Personal de Bodega ( Administrador, Auxiliar, Pasante ).")
    private TipoPersonalBodega tipoPersonalBodega;


    @OneToMany(mappedBy = "personalResponsable", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Comment("Lista de movimientos en los que el personal ha estado involucrado como responsable.")
    private List<MovimientosProducto> movimientosProducto;


    public String getRol(){
        if (this.tipoPersonalBodega == TipoPersonalBodega.AUXILIAR_BODEGA){
            return "ROLE_AUXILIAR_BODEGA";
        } else if (this.tipoPersonalBodega == TipoPersonalBodega.ADMINISTRADOR_BODEGA) {
            return "ROLE_ADMINISTRADOR_BODEGA";
        }else{
            return "ROLE_GESTOR_BODEGA";
        }
    }

}
