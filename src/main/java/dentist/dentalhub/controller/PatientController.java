package dentist.dentalhub.controller;

import dentist.dentalhub.model.Patient;
import dentist.dentalhub.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

/**
 * Controller to handle Patient module: registration, login, dashboard, and profile.
 */
@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // --------------------- REGISTER ---------------------

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "register"; // maps to register.html
    }

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient, Model model) {
        if (patientService.getPatientByEmail(patient.getEmail()) != null) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }
        patientService.savePatient(patient);
        model.addAttribute("success", "Registration successful. Please login.");
        return "login"; // redirect to login page after registration
    }

    // --------------------- LOGIN ---------------------

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "login"; // maps to login.html
    }

    @PostMapping("/login")
    public String loginPatient(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        Patient patient = patientService.getPatientByEmail(email);
        if (patient == null || !patient.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
        session.setAttribute("loggedInPatient", patient);
        return "redirect:/patients/dashboard";
    }

    // --------------------- DASHBOARD ---------------------

    @GetMapping("/dashboard")
    public String patientDashboard(HttpSession session, Model model) {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        if (patient == null) return "redirect:/patients/login";

        model.addAttribute("patient", patient);
        return "patient-dashboard"; // maps to patient-dashboard.html
    }

    // --------------------- PROFILE ---------------------

    @GetMapping("/profile")
    public String patientProfile(HttpSession session, Model model) {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        if (patient == null) return "redirect:/patients/login";

        model.addAttribute("patient", patient);
        return "patient-profile"; // maps to patient-profile.html
    }

    @PostMapping("/profile")
    public String updatePatientProfile(@ModelAttribute Patient updatedPatient,
                                       HttpSession session,
                                       Model model) {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        if (patient == null) return "redirect:/patients/login";

        patient.setFullName(updatedPatient.getFullName());
        patient.setEmail(updatedPatient.getEmail());
        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
        patient.setPassword(updatedPatient.getPassword());

        patientService.savePatient(patient);
        session.setAttribute("loggedInPatient", patient);

        model.addAttribute("success", "Profile updated successfully");
        return "patient-profile";
    }

    // --------------------- LOGOUT ---------------------

    @GetMapping("/logout")
    public String logoutPatient(HttpSession session) {
        session.invalidate();
        return "redirect:/patients/login";
    }
}