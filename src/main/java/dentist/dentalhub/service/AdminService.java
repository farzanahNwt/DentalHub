package dentist.dentalhub.service;

import dentist.dentalhub.model.Admin;
import dentist.dentalhub.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // ── Authentication ───────────────────────────────────────

    /**
     * Validate admin login credentials.
     * Returns the Admin if email and password match, otherwise empty.
     */
    public Optional<Admin> login(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent() && admin.get().getPassword().equals(password)) {
            return admin;
        }
        return Optional.empty();
    }

    // ── CRUD ─────────────────────────────────────────────────

    /** Return all admins. */
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    /** Return a single admin by ID. */
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    /** Save a new or updated admin record. */
    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    /** Delete an admin by ID. */
    public void delete(Long id) {
        adminRepository.deleteById(id);
    }
}
