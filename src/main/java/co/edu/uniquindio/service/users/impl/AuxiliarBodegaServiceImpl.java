package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
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
    private final PersonalBodegaRepo personaRepo;
    private final BodegaRepo bodegaRepo;
    private final CloudinaryService cloudinaryService;
    private final MovimientoService movimientoService;
    private final ProductoService productoService;
    private final NotificacionService notificacionService;
    private final PersonaUtilService personaUtilService;


    @Override
    public void RegistroNuevoProducto(RegistroNuevoProductoDto registroNuevoProductoDto)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoEncontradoException {

        // 1. Verificamos que el producto no exista
        if (productoRepo.existsByCodigoProducto(registroNuevoProductoDto.codigoProducto())){
            throw new ElementoRepetidoException(MensajeError.PRODUCTO_EXISTE);
        }

        // 2. Subimos imagen de perfil o asignamos una por defecto
        String urlImagen;
        if (registroNuevoProductoDto.imagenProducto() != null && !registroNuevoProductoDto.imagenProducto().isEmpty()) {
            urlImagen = cloudinaryService.uploadImage(registroNuevoProductoDto.imagenProducto());
        }else{
            throw new ElementoNulosException(MensajeError.IMAGEN_PRODUCTO_VACIA);
        }

        // 2. Verificamos que la bodega exista
        if (!bodegaRepo.existsById(Long.valueOf(registroNuevoProductoDto.idBodega()))){
            throw new ElementoNulosException(MensajeError.BODEGA_NULO);
        }

        Bodega bodega = bodegaRepo.findById(Long.parseLong(registroNuevoProductoDto.idBodega()));

        // 3, Buscamos al personal de bodega encargado del registro
        PersonalBodega personalBodega = personaUtilService.obtenerPersonalBodetaEmail(registroNuevoProductoDto.emailPersonalBodega());

        //  4, Creamos el producto y le asignamos las variables faltantes
        Producto producto = productoMapper.toEntityNew(registroNuevoProductoDto);

        producto.setImagen(urlImagen);
        producto.setBodega(bodega);

        // 5. Registramos un movimiento del producto
        MovimientosProducto movimientosProducto =
                movimientoService.registrarMovimientoProducto(
                        registroNuevoProductoDto.descripcion(), personalBodega, TipoMovimiento.PRIMER_INGRESO, producto,
                        registroNuevoProductoDto.cantidad());

        producto.getHistorialMovimientos().add(movimientosProducto);

        // 8, Guardamos en la base
        productoRepo.save(producto);

        // 9. Notificacion a las otras partes
        notificacionService.notificarMovimientoProducto("Se ha creado un ingreso de un nuevo producto "
                + producto.getCodigoProducto());

    }


    @Override
    public void AgregarCantidadProducto(RegistrarProductoExistenteDto registrarProductoExistenteDto) throws ElementoNoEncontradoException, ElementoNulosException {

        // 1. Encontramos el producto existente
        Producto producto = productoService.obtenerProductoAutorizado(registrarProductoExistenteDto.codigoProducto());

        // 4. Obtener personal encargado del registro
        PersonalBodega personalBodega = personaUtilService.obtenerPersonalBodetaEmail(registrarProductoExistenteDto.emaiPersonalBodega());

        // 5. Generamos movimiento del producto
        MovimientosProducto movimientosProducto =
                movimientoService.registrarMovimientoProducto(registrarProductoExistenteDto.descripcion(),
                        personalBodega, TipoMovimiento.INGRESO, producto,registrarProductoExistenteDto.cantidad());

        producto.getHistorialMovimientos().add(movimientosProducto);
        // 6. Guardamos en la base y esperamos la autorización.
        productoRepo.save(producto);

        // 7. Notificamos a las otras partes
        notificacionService.notificarMovimientoProducto("Se ha creado un nuevo ingreso pendiente para el producto "+ producto.getCodigoProducto());
    }


    @Override
    public void RetiroProducto(RetiroProductoDto retiroProductoDto) throws ElementoNoEncontradoException, ElementoNoValidoException {

        // 1. Encontramos el producto existente
        Producto producto = productoService.obtenerProductoAutorizado(retiroProductoDto.codigoProducto());

        // 2.1 Validamos si se puede retirar la cantidad requerida
        if (producto.getCantidad() < retiroProductoDto.cantidad()) {
            throw new ElementoNoValidoException(MensajeError.PRODUCTO_INSUFICIENTE);
        }

        // 2, Descuenta la cantidad del producto
        producto.setCantidad(producto.getCantidad() - retiroProductoDto.cantidad());

        //3. Encontramos personal encargado
        PersonalBodega personalBodega = personaUtilService.obtenerPersonalBodetaEmail(retiroProductoDto.emailPersonalResponsable());

        // 5. Generamos movimiento del producto
        MovimientosProducto movimientosProducto =
                movimientoService.registrarMovimientoProducto(retiroProductoDto.descripcion(),
                        personalBodega, TipoMovimiento.RETIRO, producto,retiroProductoDto.cantidad());

        producto.getHistorialMovimientos().add(movimientosProducto);

        // 6. Guardamos en la base y esperamos autorización.
        productoRepo.save(producto);

        // 7. Notificamos a las otras partes
        notificacionService.notificarMovimientoProducto("Se ha creado un nuevo retiro pendiente para el producto "
                + producto.getCodigoProducto());
    }

    @Override
    public ProductoDto verDetalleProducto(String codigoProducto) throws ElementoNoEncontradoException {
        return productoService.verDetalleProducto(codigoProducto);
    }


    @Override
    public List<ProductoDto> listarProductos(String codigoProducto, TipoProducto tipoProducto,
                                             EstadoProducto estadoProducto, String idBodega, int pagina, int size) {

        return productoService.listarProductos(codigoProducto, tipoProducto, estadoProducto, idBodega, pagina, size);
    }
}
