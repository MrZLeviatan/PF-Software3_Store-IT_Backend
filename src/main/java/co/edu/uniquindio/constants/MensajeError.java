package co.edu.uniquindio.constants;

public class MensajeError {


    // -----------> EMAIL <----------------
    public static final String ERROR_ENVIO_CORREO = "Error al construir o enviar el correo electrónico al destinatario: ";

    public static final String EMAIL_YA_EXISTE = "El correo electrónico ya se encuentra registrado.";

    public static final String EMAIL_CUENTA_ELIMINADA = "El correo pertenece a una cuenta eliminada.";


    // --------- > TELÉFONO <----------------
    public static final String TELEFONO_INVALIDO = "Uno o más teléfonos no son válidos para el país indicado.";

    public static final String TELEFONO_YA_EXISTENTE = "El teléfono ya está registrado";

    public static final String TELEFONO_VACIO = "El teléfono principal no puede ser nulo o vacío";


    // ---------- > PAIS <-----------------
    public static final String PAIS_NO_ENCONTRADO = "País no encontrado o no registrado en el sistema.";


    // -----------> CÓDIGO <---------------------

    public static final String CODIGO_NO_VALIDO = "El código proporcionado no coincide";

    public static final String CODIGO_EXPIRADO = "El código proporcionado a expirado";



    // -----------> LOGIN <---------------------

    public static final String PASSWORD_INCORRECTO = "La contraseña proporcionada es incorrecta";


    // -----------> Google <---------------------

    public static final String TOKEN_GOOGLE_NO_VALIDO = "El token de Google no es válido";

    // -----------> PERSONA <---------------------

    public static final String PERSONA_NO_ENCONTRADO = "Persona no encontrada o no registrada";

    public static final String DATOS_FALTANTES_REGISTRO_PERSONA = "Falta de datos para completar el registro";

    public static final String CUENTA_ELIMINADA = "La cuenta se encuentra eliminada.";

    public static final String PERSONA_NO_ROL = "Tipo de usuario desconocido";

    public static final String CUENTA_ACTIVADA = "La cuenta ya fue activada anteriormente";


    // -----------> PRODUCTO <---------------------

    public static final String PRODUCTO_EXISTE = "El producto ya se encuentra registrado";

    public static final String PRODUCTO_NO_EXISTE = "El producto NO se encuentra registrado";

    public static final String PRODUCTO_INSUFICIENTE = "La cantidad deseada a retirar supera a la cantidad en stock";

    public static final String PRODUCTO_NO_COINCIDEN = "El producto No coincide con el solicitado";


    // -----------> OTROS <---------------------


    public static final String ERROR_ELIMINAR_IMAGEN = "Error al eliminar la imagen";

    public static final String IMAGEN_PRODUCTO_VACIA = "No subio la imagen del producto";

    public static final String BODEGA_NULO = "No selecciono la bodega";


    // -----------> MOVIMIENTO <---------------------

    public static final String MOVIMIENTO_NO_EXISTE = "El movimiento No se encuentra registrado";

    public static final String TIPO_DE_MOVIMIENTO_NO_EXISTE = "Tipo de movimiento no valido";


}
