package co.edu.uniquindio.model.embeddable;

import co.edu.uniquindio.model.enums.EstadoCuenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable     // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class User {

    @Email
    @Column(name = "email", nullable = false, unique = true)
    @Comment("Dirección de correo electrónico única para autenticación.")
    private String email;

    @Column(name = "password", nullable = false)
    @Comment("Contraseña cifrada del usuario para autenticación.")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", nullable = false)
    @Comment("Estado de la cuenta: ACTIVA, INACTIVA, ELIMINADA.")
    private EstadoCuenta estadoCuenta;

    @Column(name = "registro_google")
    @Comment("Indica si el usuario se registró mediante Google.")
    private boolean registroGoogle;

    @Embedded
    private Codigo codigo;

}
