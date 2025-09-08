package co.edu.uniquindio.constants;

import co.edu.uniquindio.model.embeddable.DatosLaborales;
import co.edu.uniquindio.model.embeddable.Ubicacion;
import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.objects.Bodega;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.EstadoContratoLaboral;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoContrato;
import co.edu.uniquindio.model.enums.TipoPersonalBodega;
import co.edu.uniquindio.repository.objects.BodegaRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DatosInicialesRunner implements CommandLineRunner {


    @Autowired
    private PersonalBodegaRepo personalBodegaRepo;

    @Autowired
    private BodegaRepo bodegaRepo;

    @Override
    public void run(String... args) throws Exception {
      /**
        // Creamos dos personal de bodega quemado
        PersonalBodega personalBodegaGestor = new PersonalBodega();
        personalBodegaGestor.setNombre("Nicolas Cabrera");
        personalBodegaGestor.setTelefono("+57 3145931711");
        User user = new User();
        user.setEmail("nikis281002@gmail.com");
        user.setPassword("nicolas1234");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        personalBodegaGestor.setUser(user);

        DatosLaborales datosLaboralesGestor = new DatosLaborales();
        datosLaboralesGestor.setFechaInicioContrato(LocalDate.now());
        datosLaboralesGestor.setSueldo(BigDecimal.valueOf(3000));
        datosLaboralesGestor.setTipoContrato(TipoContrato.FIJO);
        datosLaboralesGestor.setEstadoContratoLaboral(EstadoContratoLaboral.ACTIVO);
        personalBodegaGestor.setDatosLaborales(datosLaboralesGestor);
        personalBodegaGestor.setTipoPersonalBodega(TipoPersonalBodega.GESTOR_BODEGA);
        personalBodegaRepo.save(personalBodegaGestor);

        // Creamos dos personal de bodega quemado
        PersonalBodega personalBodegaAuxiliar = new PersonalBodega();
        personalBodegaAuxiliar.setNombre("Nicolas Serrano");
        personalBodegaAuxiliar.setTelefono("+57 3144688781");
        User userAuxiliar = new User();
        user.setEmail("nicolas.cabreras@uqvirtual.edu.co");
        user.setPassword("nicolas2810");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        personalBodegaAuxiliar.setUser(user);


        DatosLaborales datosLaboralesAux = new DatosLaborales();
        datosLaboralesAux.setFechaInicioContrato(LocalDate.now());
        datosLaboralesAux.setSueldo(BigDecimal.valueOf(3000));
        datosLaboralesAux.setTipoContrato(TipoContrato.FIJO);
        datosLaboralesAux.setEstadoContratoLaboral(EstadoContratoLaboral.ACTIVO);
        personalBodegaAuxiliar.setDatosLaborales(datosLaboralesAux);
        personalBodegaAuxiliar.setTipoPersonalBodega(TipoPersonalBodega.AUXILIAR_BODEGA);
        personalBodegaRepo.save(personalBodegaAuxiliar);

        Bodega bodega = new Bodega();
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setCiudad("Medellín");
        ubicacion.setPais("Colombia");
        ubicacion.setLatitud(6.25184);
        ubicacion.setLongitud(-75.56359);
        bodega.setUbicacion(ubicacion);
        bodega.setDireccion("Calle 20 # 10");
        bodega.setTelefono("+57 311487819");
        bodegaRepo.save(bodega);

       **/
    }
       }

