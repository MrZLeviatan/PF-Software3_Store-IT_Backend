package co.edu.uniquindio.service.utils.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.exception.CargaFallidaException;
import co.edu.uniquindio.service.utils.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;


@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private Cloudinary cloudinary;

    // Inicializa Cloudinary después de inyectar las variables
    @PostConstruct
    private void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    /**
     * Sube una imagen a Cloudinary en la carpeta "storeit/productos"
     *
     * @param file archivo de imagen a subir
     * @return URL segura de la imagen subida
     */
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // Convertimos MultipartFile a File temporal
            File archivo = convertir(file);

            // Subimos a Cloudinary en la carpeta
            Map<String, Object> uploadResult = cloudinary.uploader()
                    .upload(archivo, ObjectUtils.asMap("folder", "Store-IT/ImagenesProductos"));

            // Eliminamos el archivo temporal
            archivo.delete();

            // Retornamos la URL segura
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Error subiendo imagen a Cloudinary", e);
        }
    }


    /**
     * Elimina una imagen de Cloudinary utilizando su ID.
     *
     * @param idImagen El ID de la imagen que se desea eliminar.
     * @return Un mapa con el resultado de la operación de eliminación.
     */
    @Override
    public Map eliminarImagen(String idImagen){
        try {
            return cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
        }catch (Exception e) {
            throw new CargaFallidaException(MensajeError.ERROR_ELIMINAR_IMAGEN, e);
        }}




    // Método auxiliar para convertir MultipartFile a File
    private File convertir(MultipartFile imagen) throws IOException {
        File file = File.createTempFile(imagen.getOriginalFilename(), null);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imagen.getBytes());
        }
        return file;
    }
}

