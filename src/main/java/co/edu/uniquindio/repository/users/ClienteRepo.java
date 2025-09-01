package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.model.entities.users.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepo extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {


    Optional<Cliente> findByUser_Email(String email);

    Optional<Cliente> findByTelefono(String telefono);

    Optional<Cliente> findByTelefonoSecundario(String telefonoSecundario);

    boolean existsByTelefonoOrTelefonoSecundario(String telefono, String telefonoSecundario);

    boolean existsByTelefono(String telefono);

}
