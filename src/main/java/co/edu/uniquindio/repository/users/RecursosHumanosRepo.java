package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.model.entities.users.RecursosHumanos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RecursosHumanosRepo extends JpaRepository<RecursosHumanos, Long>, JpaSpecificationExecutor<RecursosHumanos> {


    Optional<RecursosHumanos> findByUser_Email(String email);

    Optional<RecursosHumanos> findByTelefono(String telefono);

    Optional<RecursosHumanos> findByTelefonoSecundario(String telefonoSecundario);

    boolean existsByTelefonoOrTelefonoSecundario(String telefono, String telefonoSecundario);

    boolean existsByTelefono(String telefono);


}
