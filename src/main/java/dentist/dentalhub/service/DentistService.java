package dentist.dentalhub.service;

import dentist.dentalhub.model.Dentist;
import dentist.dentalhub.repository.DentistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DentistService {

    @Autowired
    private DentistRepository dentistRepository;

    // ── Authentication ───────────────────────────────────────

    /**
     * Validate dentist login credentials.
     * Returns the Dentist if email and password match, otherwise empty.
     */
    public Optional<Dentist> login(String email, String password) {
        Optional<Dentist> dentist = dentistRepository.findByEmail(email);
        if (dentist.isPresent() && dentist.get().getPassword().equals(password)) {
            return dentist;
        }
        return Optional.empty();
    }

    // ── CRUD ─────────────────────────────────────────────────

    /** Return all dentists — used by admin manage-users page. */
    public List<Dentist> getAllDentists() {
        return dentistRepository.findAll();
    }

    /** Return a single dentist by ID. */
    public Optional<Dentist> getDentistById(Long id) {
        return dentistRepository.findById(id);
    }

    /** Save a new or updated dentist record. */
    public Dentist save(Dentist dentist) {
        return dentistRepository.save(dentist);
    }

    /** Delete a dentist by ID. */
    public void delete(Long id) {
        dentistRepository.deleteById(id);
    }

    /** Check if email is already taken (e.g., during profile update). */
    public boolean emailExists(String email) {
        return dentistRepository.existsByEmail(email);
    }
}
