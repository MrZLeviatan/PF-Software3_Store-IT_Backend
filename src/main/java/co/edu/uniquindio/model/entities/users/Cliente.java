package co.edu.uniquindio.model.entities.users;

import co.edu.uniquindio.model.embeddable.Ubicacion;
import co.edu.uniquindio.model.enums.TipoCliente;
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
@Table(name = "clientes")
@Comment("Entidad que representa a un cliente que firma contratos con la empresa.")
public class Cliente extends Persona{

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    @Comment("Tipo Cliente que contrata el servicio. Natural o Jurídica.")
    private TipoCliente tipoCliente;

    @Column(name = "NIT_empresa", nullable = true)
    @Comment("NIT del cliente si es Juridico.")
    private String nit;

    @Embedded
    private Ubicacion ubicacion;

}
