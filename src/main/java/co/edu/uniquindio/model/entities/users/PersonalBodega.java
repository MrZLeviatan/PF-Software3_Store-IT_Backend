package co.edu.uniquindio.model.entities.users;


import co.edu.uniquindio.model.embeddable.DatosLaborales;
import co.edu.uniquindio.model.enums.TipoPersonalBodega;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

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


}
