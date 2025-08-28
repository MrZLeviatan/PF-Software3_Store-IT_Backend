package co.edu.uniquindio.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class Ubicacion {


    @Column(name = "pais")
    @Comment("País de residencia.")
    private String pais;

    @Column(name = "ciudad")
    @Comment("Ciudad de residencia.")
    private String ciudad;

    @Column(name = "latitud")
    @Comment("Latitud geográfica de la ubicación.")
    private Double latitud;

    @Column(name = "longitud")
    @Comment("Longitud geográfica de la ubicación.")
    private Double longitud;

}
