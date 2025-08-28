package co.edu.uniquindio.model.embeddable;

import co.edu.uniquindio.model.enums.TipoCodigo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class CodigoRestablecimiento {

    @Column(name = "codigo_restablecimiento")
    private String codigoRestablecimiento;

    @Column(name = "fecha_restablecimiento_expiracion")
    private LocalDateTime fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_codigo")
    private TipoCodigo tipoCodigo;

}
