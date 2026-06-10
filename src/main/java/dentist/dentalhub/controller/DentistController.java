package dentist.dentalhub.controller;

import dentist.dentalhub.model.Dentist;
import dentist.dentalhub.service.DentistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/dentists")
public class DentistController {

    @Autowired
    private DentistService dentistService;

    // ── Login ────────────────────────────────────────────────

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // reuse shared login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        Optional<Dentist> dentist = dentistService.login(email, password);
        if (dentist.isPresent()) {
            session.setAttribute("dentist", dentist.get());
            return "redirect:/dentists/dashboard";
        }
        model.addAttribute("error", "Invalid email or password.");
        return "login";
    }

    // ── Logout ───────────────────────────────────────────────

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/dentists/login";
    }

    // ── Dashboard ────────────────────────────────────────────

    /**
     * Dentist dashboard — shows the dentist's appointments grouped by status.
     * The Appointment data is owned by Member 2; we retrieve it via session/model.
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Dentist dentist = (Dentist) session.getAttribute("dentist");
        if (dentist == null) return "redirect:/dentists/login";

        model.addAttribute("dentist", dentist);
        // Appointment list is populated by AppointmentService (Member 2)
        // Here we pass the dentist ID so the view can call the correct endpoint
        return "dentist-dashboard";
    }

    // ── Profile update ───────────────────────────────────────

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute Dentist updated,
                                HttpSession session,
                                Model model) {
        Dentist dentist = (Dentist) session.getAttribute("dentist");
        if (dentist == null) return "redirect:/dentists/login";

        dentist.setFullName(updated.getFullName());
        dentist.setPhoneNumber(updated.getPhoneNumber());
        dentist.setSpecialization(updated.getSpecialization());
        dentistService.save(dentist);
        session.setAttribute("dentist", dentist);

        model.addAttribute("dentist", dentist);
        model.addAttribute("success", "Profile updated successfully.");
        return "dentist-dashboard";
    }
}
