package co.edu.uniquindio.service.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {


    String uploadImage(MultipartFile file);

    Map eliminarImagen(String idImagen);


}
