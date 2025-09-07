package co.edu.uniquindio.service.objects;

import co.edu.uniquindio.dto.objects.producto.ProductoDto;
import co.edu.uniquindio.model.enums.EstadoProducto;
import co.edu.uniquindio.model.enums.TipoProducto;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductoService {


    ProductoDto verDetalleProducto(String codigoProducto);

    List<ProductoDto> listarProductos (
            String codigoProducto, TipoProducto tipoProducto, LocalDateTime fechaVencimiento,
            EstadoProducto estadoProducto, String idBodega);


}
