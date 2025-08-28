package co.edu.uniquindio.model.entities.users;

import co.edu.uniquindio.model.embeddable.User;
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
@MappedSuperclass   // Indica que esta clase es una superclase de entidades, pero no se mapea como tabla.
public abstract class Persona {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID creado automáticamente por Oracle SQL
    @Comment("ID interno único de la persona creado automáticamente por el sistema.")
    private Long id;

    @Column(name = "nombre_completo", nullable = false) // El nombre no puede ser nulo.
    @Comment("Nombre completo del usuario (nombre y apellidos). Si es empresa, nombre de la empresa.")
    private String nombre;

    @Column(name = "teléfono", nullable = false)
    @Comment("Número telefónico principal de contacto.")
    private String telefono;

    @Column(name = "teléfono_secundario", nullable = false)
    @Comment("Número telefónico principal de contacto (Opcional).")
    private String telefonoSecundario;

    @Embedded // Componente que se adhiere a la clase
    private User user;

}
