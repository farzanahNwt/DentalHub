package dentist.dentalhub.service;

import dentist.dentalhub.model.Appointment;
import dentist.dentalhub.model.Patient;
import dentist.dentalhub.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment bookAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getHistory(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId);
    }

    // Farzanah added this — used by PatientController dashboard
    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patient.getPatientId());
    }


    public Optional<Appointment> getAppointmentDetails(Long appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    public void cancelAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }
}