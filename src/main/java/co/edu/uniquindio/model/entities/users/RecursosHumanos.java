package co.edu.uniquindio.model.entities.users;


import co.edu.uniquindio.model.embeddable.DatosLaborales;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recursos_humanos")
public class RecursosHumanos extends Persona{


    @Embedded
    private DatosLaborales datosLaborales;


}
