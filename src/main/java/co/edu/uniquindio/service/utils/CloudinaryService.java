package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exception.ElementoNoValidoException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {


    String uploadImage(MultipartFile file) throws ElementoNoValidoException;

    Map eliminarImagen(String idImagen);


}
