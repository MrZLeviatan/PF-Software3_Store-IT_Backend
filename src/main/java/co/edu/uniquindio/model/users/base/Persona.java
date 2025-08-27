package co.edu.uniquindio.model.users.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Persona {

    private Long id;
    private String nombre;
    private String telefono;
    private String telefonoSecundario;
    private String imagenPerfil;

}
