package dentist.dentalhub.repository;

import dentist.dentalhub.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /** Find admin by email — used for login authentication. */
    Optional<Admin> findByEmail(String email);

    /** Check if an email already exists. */
    boolean existsByEmail(String email);
}
