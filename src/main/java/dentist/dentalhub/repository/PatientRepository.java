package dentist.dentalhub.repository;

import dentist.dentalhub.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// PatientRepository handles database operations for the Patient entity
// This allows CRUD operations and finding patients by email

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find a patient by their email address.
    Patient findByEmail(@Param("email") String email);
}