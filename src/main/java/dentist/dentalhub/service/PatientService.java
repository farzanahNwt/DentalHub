package dentist.dentalhub.service;

import dentist.dentalhub.model.Patient;
import dentist.dentalhub.repository.PatientRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

// PatientService handles the business logic for Patient entity.
// All interactions with the PatientRepository go through this service

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(@Param("id") Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient getPatientByEmail(@Param("email") String email) {
        return patientRepository.findByEmail(email);
    }

    public void deletePatientById(@Param("id") Long id) {
        patientRepository.deleteById(id);
    }

}
