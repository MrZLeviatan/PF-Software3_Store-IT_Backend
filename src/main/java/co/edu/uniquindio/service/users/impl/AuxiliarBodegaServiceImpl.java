package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.objects.bodega.BodegaDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.producto.RegistrarProductoExistenteDto;
import co.edu.uniquindio.dto.objects.producto.RegistroNuevoProductoDto;
import co.edu.uniquindio.dto.objects.producto.RetiroProductoDto;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.exception.ElementoNoValidoException;
import co.edu.uniquindio.exception.ElementoNulosException;
import co.edu.uniquindio.exception.ElementoRepetidoException;
import co.edu.uniquindio.mapper.objects.ProductoMapper;
import co.edu.uniquindio.model.entities.objects.Bodega;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import co.edu.uniquindio.model.enums.TipoProducto;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.repository.objects.ProductoRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.service.objects.BodegaService;
import co.edu.uniquindio.service.objects.MovimientoService;
import co.edu.uniquindio.service.objects.NotificacionService;
import co.edu.uniquindio.service.objects.ProductoService;
import co.edu.uniquindio.service.users.AuxiliarBodegaService;
import co.edu.uniquindio.service.utils.CloudinaryService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuxiliarBodegaServiceImpl implements AuxiliarBodegaService {

    private final ProductoMapper productoMapper;
    private final ProductoRepo productoRepo;
    private final BodegaRepo bodegaRepo;
    private final CloudinaryService cloudinaryService;
    private final MovimientoService movimientoService;
    private final ProductoService productoService;
    private final NotificacionService notificacionService;
    private final PersonaUtilService personaUtilService;
    private final BodegaService bodegaService;


    /** Registra un nuevo producto en el sistema.
    //   Valida si ya existe, sube la imagen al servicio en la nube,
    //   asocia la bodega y el personal de registro, crea el producto
    //  y registra su primer movimiento.
     */
    @Override
    public void RegistroNuevoProducto(RegistroNuevoProductoDto registroNuevoProductoDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoEncontradoException, ElementoNoValidoException {

        // 1. Verificamos que el producto no exista
        if (productoRepo.existsByCodigoProducto(registroNuevoProductoDto.codigoProducto())){
            throw new ElementoRepetidoException(MensajeError.PRODUCTO_EXISTE);
        }

        // 2. Subimos imagen de perfil o lanzamos excepción si no se envía
        String urlImagen;
        if (registroNuevoProductoDto.imagenProducto() != null && !registroNuevoProductoDto.imagenProducto().isEmpty()) {
            urlImagen = cloudinaryService.uploadImage(registroNuevoProductoDto.imagenProducto());
        } else {
            throw new ElementoNulosException(MensajeError.IMAGEN_PRODUCTO_VACIA);
        }

        // 3. Verificamos que la bodega exista
        if (!bodegaRepo.existsById(Long.valueOf(registroNuevoProductoDto.idBodega()))){
            throw new ElementoNulosException(MensajeError.BODEGA_NULO);
        }

        Bodega bodega = bodegaRepo.findById(Long.parseLong(registroNuevoProductoDto.idBodega()));

        // 4. Buscamos al personal de bodega encargado
        PersonalBodega personalBodega = personaUtilService.obtenerPersonalBodetaEmail(registroNuevoProductoDto.emailPersonalBodega());

        // 5. Creamos el producto y asignamos sus datos
        Producto producto = productoMapper.toEntityNew(registroNuevoProductoDto);
        producto.setImagen(urlImagen);
        producto.setBodega(bodega);

        productoRepo.save(producto);

        // 5. Registramos un movimiento del producto
        MovimientosProducto movimientosProducto =
                movimientoService.registrarMovimientoProducto(
                        registroNuevoProductoDto.descripcion(), personalBodega, TipoMovimiento.PRIMER_INGRESO, producto,
                        registroNuevoProductoDto.cantidad());

        producto.getHistorialMovimientos().add(movimientosProducto);

        // 7. Guardamos en la base
        productoRepo.save(producto);

        // 8. Enviamos notificación del nuevo producto
        notificacionService.notificarMovimientoProducto("Se ha creado un ingreso de un nuevo producto "
                + producto.getCodigoProducto());
    }

    /** Agrega cantidad a un producto existente.
    //   Verifica que el producto esté autorizado, registra el movimiento
    //   y guarda los cambios en espera de autorización.
     */
    @Override
    public void AgregarCantidadProducto(RegistrarProductoExistenteDto registrarProductoExistenteDto)
            throws ElementoNoEncontradoException, ElementoNulosException {

        // 1. Encontramos el producto existente
        Producto producto = productoService.obtenerProductoAutorizado(registrarProductoExistenteDto.codigoProducto());

        // 2. Obtenemos personal encargado
        PersonalBodega personalBodega = personaUtilService.obtenerPersonalBodetaEmail(registrarProductoExistenteDto.emailPersonalBodega());


        // 3. Registramos el movimiento
        MovimientosProducto movimientosProducto =
                movimientoService.registrarMovimientoProducto(registrarProductoExistenteDto.descripcion(),
                        personalBodega, TipoMovimiento.INGRESO, producto, registrarProductoExistenteDto.cantidad());

        producto.getHistorialMovimientos().add(movimientosProducto);

        // 4. Guardamos en base de datos
        productoRepo.save(producto);

        // 5. Notificamos el ingreso
        notificacionService.notificarMovimientoProducto("Se ha creado un nuevo ingreso pendiente para el producto "
                + producto.getCodigoProducto());
    }

    /** Realiza el retiro de un producto.
    //   Valida que la cantidad sea suficiente, descuenta inventario,
    //   registra el movimiento y guarda los cambios en espera de autorización.
     */
    @Override
    public void RetiroProducto(RetiroProductoDto retiroProductoDto)
            throws ElementoNoEncontradoException, ElementoNoValidoException {

        // 1. Encontramos el producto existente
        Producto producto = productoService.obtenerProductoAutorizado(retiroProductoDto.codigoProducto());

        // 2. Validamos la cantidad disponible
        if (producto.getCantidad() < retiroProductoDto.cantidad()) {
            throw new ElementoNoValidoException(MensajeError.PRODUCTO_INSUFICIENTE);
        }

        // 3. Actualizamos la cantidad
        producto.setCantidad(producto.getCantidad() - retiroProductoDto.cantidad());

        // 4. Obtenemos personal encargado
        PersonalBodega personalBodega = personaUtilService.obtenerPersonalBodetaEmail(retiroProductoDto.emailPersonalResponsable());

        // 5. Registramos el movimiento
        MovimientosProducto movimientosProducto =
                movimientoService.registrarMovimientoProducto(retiroProductoDto.descripcion(),
                        personalBodega, TipoMovimiento.RETIRO, producto, retiroProductoDto.cantidad());

        producto.getHistorialMovimientos().add(movimientosProducto);

        // 6. Guardamos cambios
        productoRepo.save(producto);

        // 7. Notificamos el retiro
        notificacionService.notificarMovimientoProducto("Se ha creado un nuevo retiro pendiente para el producto "
                + producto.getCodigoProducto());
    }

    // Consulta los detalles de un producto específico
    @Override
    public ProductoDto verDetalleProducto(String codigoProducto) throws ElementoNoEncontradoException {
        return productoService.verDetalleProducto(codigoProducto);
    }

    // Lista productos aplicando filtros como código, tipo, estado y bodega
    @Override
    public List<ProductoDto> listarProductos(String codigoProducto, TipoProducto tipoProducto,
                                             EstadoProducto estadoProducto, String idBodega, int pagina, int size) {


        return productoService.listarProductosAutorizados(codigoProducto, tipoProducto, estadoProducto, idBodega, pagina, size);
    }

    @Override
    public List<BodegaDto> obtenerBodegas() {
        return bodegaService.listarTodasBodegas();
    }
}
