package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.model.entities.users.PersonalBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PersonalBodegaRepo extends JpaRepository<PersonalBodega, Long>, JpaSpecificationExecutor<PersonalBodega> {


    Optional<PersonalBodega> findByUser_Email(String email);

    Optional<PersonalBodega> findByTelefono(String telefono);

    Optional<PersonalBodega> findByTelefonoSecundario(String telefonoSecundario);

    boolean existsByTelefonoOrTelefonoSecundario(String telefono, String telefonoSecundario);

    boolean existsByTelefono(String telefono);



}
