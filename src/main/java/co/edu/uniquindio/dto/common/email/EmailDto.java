package co.edu.uniquindio.dto.common.email;

public record EmailDto(

        String destinatario,
        String cuerpo,  // Sirve para enviar tanto el código como un cuerpo
        String asunto

) {
}
