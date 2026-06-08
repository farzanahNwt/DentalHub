package dentist.dentalhub.repository;

import dentist.dentalhub.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Notice how the method name here perfectly matches your SQL schema column fields!
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(Long patientId);

    List<Appointment> findByDentistId(Long dentistId);
}