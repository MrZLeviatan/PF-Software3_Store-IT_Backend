package co.edu.uniquindio.constants;

import co.edu.uniquindio.model.embeddable.User;
import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.EstadoCuenta;
import co.edu.uniquindio.model.enums.TipoPersonalBodega;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosInicialesRunner implements CommandLineRunner {


    @Autowired
    private PersonalBodegaRepo personalBodegaRepo;

    @Override
    public void run(String... args) throws Exception {

        // Creamos dos personal de bodega quemado
        PersonalBodega personalBodegaGestor = new PersonalBodega();
        personalBodegaGestor.setNombre("Nicolas Cabrera");
        personalBodegaGestor.setTelefono("+57 3145931711");
        User user = new User();
        user.setEmail("nikis281002@gmail.com");
        user.setPassword("nicolas1234");
        user.setEstadoCuenta(EstadoCuenta.ACTIVO);
        personalBodegaGestor.setUser(user);
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
        personalBodegaAuxiliar.setTipoPersonalBodega(TipoPersonalBodega.AUXILIAR_BODEGA);
        personalBodegaRepo.save(personalBodegaAuxiliar);
    }
}
