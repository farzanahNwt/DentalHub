package dentist.dentalhub.controller;

import dentist.dentalhub.model.Admin;
import dentist.dentalhub.model.Dentist;
import dentist.dentalhub.service.AdminService;
import dentist.dentalhub.service.DentistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DentistService dentistService;

    // ── Login ────────────────────────────────────────────────

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        Optional<Admin> admin = adminService.login(email, password);
        if (admin.isPresent()) {
            session.setAttribute("admin", admin.get());
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid email or password.");
        return "login";
    }

    // ── Logout ───────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // ── Dashboard ────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/admin/login";

        model.addAttribute("admin", admin);
        model.addAttribute("dentists", dentistService.getAllDentists());
        // Patient list is owned by Member 1 — passed via PatientService if wired
        return "admin-dashboard";
    }

    // ── Manage Users ─────────────────────────────────────────

    @GetMapping("/manage-users")
    public String manageUsers(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        model.addAttribute("dentists", dentistService.getAllDentists());
        // Patients list would be injected by Member 1's PatientService
        return "manage-users";
    }

    /** Admin adds a new dentist. */
    @PostMapping("/manage-users/add-dentist")
    public String addDentist(@ModelAttribute Dentist dentist,
                             HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        dentistService.save(dentist);
        return "redirect:/admin/manage-users";
    }

    /** Admin deletes a dentist. */
    @PostMapping("/manage-users/delete-dentist/{id}")
    public String deleteDentist(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";
        dentistService.delete(id);
        return "redirect:/admin/manage-users";
    }

    // ── Manage Schedules ─────────────────────────────────────

    @GetMapping("/manage-schedules")
    public String manageSchedules(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        model.addAttribute("dentists", dentistService.getAllDentists());
        // Appointments are fetched by Member 2's AppointmentService
        return "manage-schedules";
    }
}
