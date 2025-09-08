package co.edu.uniquindio.service.users.impl;

import co.edu.uniquindio.constants.MensajeError;
import co.edu.uniquindio.dto.objects.movimientoProducto.MovimientosProductoDto;
import co.edu.uniquindio.dto.objects.producto.AutorizacionProductoDto;
import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.exception.ElementoIncorrectoException;
import co.edu.uniquindio.exception.ElementoNoCoincideException;
import co.edu.uniquindio.exception.ElementoNoEncontradoException;
import co.edu.uniquindio.model.entities.objects.MovimientosProducto;
import co.edu.uniquindio.model.entities.objects.Producto;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.EstadoMovimiento;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoMovimiento;
import co.edu.uniquindio.model.enums.TipoProducto;
import co.edu.uniquindio.repository.objects.MovimientoProductoRepo;
import co.edu.uniquindio.repository.objects.ProductoRepo;
import co.edu.uniquindio.service.objects.MovimientoService;
import co.edu.uniquindio.service.objects.NotificacionService;
import co.edu.uniquindio.service.objects.ProductoService;
import co.edu.uniquindio.service.users.GestorBodegasService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * Implementación del servicio de gestión de bodegas.
 * Aquí se desarrollan las reglas de negocio para la autorización de movimientos
 * de productos (entradas/salidas), validaciones y notificaciones al personal.
 */
public class GestorBodegasServiceImpl implements GestorBodegasService {

    private final MovimientoService movimientoService;
    private final MovimientoProductoRepo movimientoProductoRepo;
    private final ProductoService productoService;
    private final ProductoRepo productoRepo;
    private final NotificacionService notificacionService;
    private final PersonaUtilService personaUtilService;

    @Override
    public void autorizarMovimiento(AutorizacionProductoDto autorizacionProductoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException, ElementoIncorrectoException {

        // 1. Obtener el movimiento de producto a partir del ID
        MovimientosProducto movimientosProducto = movimientoService
                .obtenerMovimiento(Long.valueOf(autorizacionProductoDto.idMovimiento()));

        // 1.2 Validar que el producto concuerde con el movimiento
        Producto producto = productoService.obtenerProducto(autorizacionProductoDto.codigoProducto());
        if (!producto.equals(movimientosProducto.getProducto())) {
            throw new ElementoNoCoincideException(MensajeError.PRODUCTO_NO_COINCIDEN);
        }

        // 1.3 Buscar el personal que autoriza el movimiento
        PersonalBodega personalAutorizado =
                personaUtilService.obtenerPersonalBodetaEmail(autorizacionProductoDto.emailPersonalAutorizado());

        String mensaje;
        // 2. Según el estado, verificar o denegar el movimiento
        if (autorizacionProductoDto.estadoMovimiento().equals(EstadoMovimiento.VERIFICADO)) {
            verificarMovimiento(producto, movimientosProducto, personalAutorizado,
                    autorizacionProductoDto.descripcionAutorizacion());
            mensaje = "El movimiento del producto " + producto.getCodigoProducto() + " ha sido autorizado.";
        } else {
            denegarMovimiento(producto, movimientosProducto, personalAutorizado,
                    autorizacionProductoDto.descripcionAutorizacion());
            mensaje = "El movimiento del producto " + producto.getCodigoProducto() + " ha sido denegado.";
        }

        // 3. Reemplazar el movimiento en el historial del producto
        producto.getHistorialMovimientos().replaceAll(m ->
                m.getId().equals(movimientosProducto.getId()) ? movimientosProducto : m
        );

        // 4. Guardar los cambios en la base de datos
        productoRepo.save(producto);

        // 5. Notificar al auxiliar que hizo la solicitud del movimiento
        notificacionService.notificarCambioMovimiento(movimientosProducto.getPersonalResponsable(), mensaje);
    }

    /*
     * Verifica un movimiento (autorización positiva).
     * Dependiendo del tipo de movimiento, se actualiza el estado o la cantidad del producto.
     */
    private void verificarMovimiento(Producto producto,
                                     MovimientosProducto movimientosProducto,
                                     PersonalBodega personalAutorizado,
                                     String descripcion) throws ElementoIncorrectoException {

        // Actualizar producto según el tipo de movimiento
        switch (movimientosProducto.getTipoMovimiento()) {
            case TipoMovimiento.PRIMER_INGRESO -> producto.setAutorizado(true);
            case TipoMovimiento.INGRESO -> producto.setCantidad(movimientosProducto.getCantidad() + producto.getCantidad());
            case TipoMovimiento.RETIRO -> producto.setCantidad(movimientosProducto.getCantidad() - producto.getCantidad());
            default -> throw new ElementoIncorrectoException(MensajeError.TIPO_DE_MOVIMIENTO_NO_EXISTE);
        }

        // Marcar movimiento como verificado
        movimientosProducto.setVerificado(true);
        movimientosProducto.setPersonalAutorizacion(personalAutorizado);
        movimientosProducto.setFechaAutorizacion(LocalDateTime.now());
        movimientosProducto.setDescripcionAutorizado(descripcion);
        movimientoProductoRepo.save(movimientosProducto);
    }

    /*
     * Deniega un movimiento (autorización negativa).
     * Dependiendo del tipo de movimiento, se revierten los cambios en el producto.
     */
    private void denegarMovimiento(Producto producto,
                                   MovimientosProducto movimientosProducto,
                                   PersonalBodega personalAutorizado,
                                   String descripcion) throws ElementoIncorrectoException {

        // Revertir cambios según el tipo de movimiento
        switch (movimientosProducto.getTipoMovimiento()) {
            case TipoMovimiento.PRIMER_INGRESO -> producto.setEstadoProducto(EstadoProducto.ELIMINADO);
            case TipoMovimiento.RETIRO -> producto.setCantidad(movimientosProducto.getCantidad() + producto.getCantidad());
            default -> throw new ElementoIncorrectoException(MensajeError.TIPO_DE_MOVIMIENTO_NO_EXISTE);
        }

        // Marcar movimiento como verificado (aunque denegado)
        movimientosProducto.setVerificado(true);
        movimientosProducto.setPersonalAutorizacion(personalAutorizado);
        movimientosProducto.setFechaAutorizacion(LocalDateTime.now());
        movimientosProducto.setDescripcionAutorizado(descripcion);
        movimientoProductoRepo.save(movimientosProducto);
    }

    @Override
    public ProductoDto verDetalleProducto(String codigoProducto) throws ElementoNoEncontradoException {
        return productoService.verDetalleProducto(codigoProducto);
    }

    @Override
    public List<ProductoDto> listarProductos(String codigoProducto, TipoProducto tipoProducto,
                                             EstadoProducto estadoProducto, String idBodega,
                                             int pagina, int size) {
        return productoService.listarProductos(codigoProducto, tipoProducto,
                estadoProducto, idBodega, pagina, size);
    }

    @Override
    public MovimientosProductoDto verDetallesMovimiento(Long idMovimientosProducto)
            throws ElementoNoEncontradoException {
        return movimientoService.verDetalleMovimiento(idMovimientosProducto);
    }

    @Override
    public List<MovimientosProductoDto> obtenerMovimientosProducto(String codigoProducto,
                                                                   TipoMovimiento tipoMovimiento,
                                                                   LocalDateTime fechaMovimiento,
                                                                   String emailPersonalResponsable,
                                                                   String emailPersonalAutorizado,
                                                                   String idBodega,
                                                                   int pagina, int size) {
        return movimientoService.listarMovimientos(codigoProducto, tipoMovimiento, fechaMovimiento,
                emailPersonalResponsable, emailPersonalAutorizado, idBodega, pagina, size);
    }
}
