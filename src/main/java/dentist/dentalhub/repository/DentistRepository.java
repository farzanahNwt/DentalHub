package dentist.dentalhub.repository;

import dentist.dentalhub.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {

    /** Find dentist by email — used for login authentication. */
    Optional<Dentist> findByEmail(String email);

    /** Check if an email already exists — used during registration. */
    boolean existsByEmail(String email);
}
