package co.edu.uniquindio.model.embeddable;

import co.edu.uniquindio.model.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.enums.TipoContrato;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor  
@Embeddable     // Esta clase será embebida en otras entidades, no se convierte en una tabla
public class DatosLaborales {

    @Column(name = "fecha_inicio_contrato", nullable = false)
    @Comment("Fecha de inicio de contratación.")
    private LocalDate fechaInicioContrato;

    @Column(name = "fecha_fin_contrato", nullable = true)
    @Comment("Fecha de finalización del contrato (si aplica).")
    private LocalDate fechaFinContrato;

    @Column(name = "sueldo", nullable = false)
    private BigDecimal sueldo;

    @Email
    @Column(name = "email_empresarial", nullable = false, unique = true)
    @Comment("Dirección de correo electrónico empresarial única para autenticación.")
    private String emailEmpresarial;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato", nullable = false)
    @Comment("Tipo de contrato: INDEFINIDO, FIJO, TEMPORAL, PRESTACION_SERVICIOS.")
    private TipoContrato tipoContrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_contrato")
    @Comment("Estado del contrato: ACTIVO, SUSPENDIDO, FINALIZADO.")
    private EstadoContratoLaboral estadoContratoLaboral;

}
