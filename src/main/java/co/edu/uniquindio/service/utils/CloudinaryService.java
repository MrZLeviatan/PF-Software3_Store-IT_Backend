package co.edu.uniquindio.service.utils;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/** Servicio utilitario para la gestión de imágenes en Cloudinary.
   Permite cargar y eliminar imágenes en la nube. */
public interface CloudinaryService {

    // Sube una imagen a Cloudinary y retorna la URL pública de la imagen almacenada.
    String uploadImage(MultipartFile file);
    /** Elimina una imagen de Cloudinary a partir de su identificador único (public_id).
       Retorna un mapa con el resultado de la operación. */
    Map eliminarImagen(String idImagen);
}
