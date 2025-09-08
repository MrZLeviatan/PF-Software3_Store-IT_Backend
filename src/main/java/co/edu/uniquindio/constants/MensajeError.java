package co.edu.uniquindio.constants;

/**
 * clase para mostrar emensajes de errores
 */
package co.edu.uniquindio.util;

public class MensajeError {

    // -----------> EMAIL <----------------
    public static final String ERROR_ENVIO_CORREO = "Se produjo un error al construir o enviar el correo electrónico al destinatario.";
    public static final String EMAIL_YA_EXISTE = "El correo electrónico ya está registrado en el sistema.";
    public static final String EMAIL_CUENTA_ELIMINADA = "El correo electrónico corresponde a una cuenta eliminada.";


    // -----------> TELÉFONO <----------------
    public static final String TELEFONO_INVALIDO = "Uno o más números de teléfono no son válidos para el país especificado.";
    public static final String TELEFONO_YA_EXISTENTE = "El número de teléfono ya está registrado en el sistema.";
    public static final String TELEFONO_VACIO = "El número de teléfono principal es obligatorio y no puede estar vacío.";


    // -----------> PAÍS <-----------------
    public static final String PAIS_NO_ENCONTRADO = "El país especificado no fue encontrado o no está registrado en el sistema.";


    // -----------> CÓDIGO <---------------------
    public static final String CODIGO_NO_VALIDO = "El código proporcionado no es válido.";
    public static final String CODIGO_EXPIRADO = "El código proporcionado ha expirado.";


    // -----------> LOGIN <---------------------
    public static final String PASSWORD_INCORRECTO = "La contraseña ingresada es incorrecta.";


    // -----------> GOOGLE <---------------------
    public static final String TOKEN_GOOGLE_NO_VALIDO = "El token de autenticación de Google no es válido.";


    // -----------> PERSONA <---------------------
    public static final String PERSONA_NO_ENCONTRADO = "La persona especificada no fue encontrada o no está registrada en el sistema.";
    public static final String DATOS_FALTANTES_REGISTRO_PERSONA = "Faltan datos obligatorios para completar el registro de la persona.";
    public static final String CUENTA_ELIMINADA = "La cuenta está eliminada y no puede ser utilizada.";
    public static final String PERSONA_NO_ROL = "El tipo de usuario especificado no es válido o no está reconocido.";
    public static final String CUENTA_ACTIVADA = "La cuenta ya fue activada previamente.";


    // -----------> PRODUCTO <---------------------
    public static final String PRODUCTO_EXISTE = "El producto ya está registrado en el sistema.";
    public static final String PRODUCTO_NO_EXISTE = "El producto especificado no se encuentra registrado en el sistema.";
    public static final String PRODUCTO_INSUFICIENTE = "La cantidad solicitada supera el stock disponible.";
    public static final String PRODUCTO_NO_COINCIDEN = "El producto no coincide con el solicitado.";


    // -----------> OTROS <---------------------
    public static final String ERROR_ELIMINAR_IMAGEN = "Se produjo un error al intentar eliminar la imagen.";
    public static final String IMAGEN_PRODUCTO_VACIA = "No se proporcionó ninguna imagen para el producto.";
    public static final String BODEGA_NULO = "Debe seleccionar una bodega válida.";


    // -----------> MOVIMIENTO <---------------------
    public static final String MOVIMIENTO_NO_EXISTE = "El movimiento especificado no se encuentra registrado en el sistema.";
    public static final String TIPO_DE_MOVIMIENTO_NO_EXISTE = "El tipo de movimiento especificado no es válido.";
}


