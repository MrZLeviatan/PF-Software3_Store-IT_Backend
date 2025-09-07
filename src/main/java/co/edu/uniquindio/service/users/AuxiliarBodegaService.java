package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.objects.producto.RegistrarProductoExistenteDto;
import co.edu.uniquindio.dto.objects.producto.RegistroNuevoProductoDto;
import co.edu.uniquindio.dto.objects.producto.RetiroProductoDto;

public interface AuxiliarBodegaService {



    void RegistroNuevoProducto(RegistroNuevoProductoDto registroNuevoProductoDto);

    void AgregarCantidadProducto(RegistrarProductoExistenteDto registrarProductoExistenteDto);

    void RetiroProducto(RetiroProductoDto retiroProductoDto);



}
