package co.edu.uniquindio.repository.users;

import co.edu.uniquindio.model.entities.users.PersonalBodega;
import co.edu.uniquindio.model.enums.TipoPersonalBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PersonalBodegaRepo extends JpaRepository<PersonalBodega, Long>, JpaSpecificationExecutor<PersonalBodega> {


    Optional<PersonalBodega> findByUser_Email(String email);

    Optional<PersonalBodega> findByTelefono(String telefono);

    Optional<PersonalBodega> findByTelefonoSecundario(String telefonoSecundario);

    Optional<PersonalBodega> findByTipoPersonalBodega(TipoPersonalBodega tipoPersonalBodega);

    Optional<List<PersonalBodega>> findAllByTipoPersonalBodega(TipoPersonalBodega tipoPersonalBodega);

    boolean existsByTelefonoOrTelefonoSecundario(String telefono, String telefonoSecundario);

    boolean existsByTelefono(String telefono);

    List<PersonalBodega> findByTipoPersonalBodegaIn(List<TipoPersonalBodega> tipos);

}
