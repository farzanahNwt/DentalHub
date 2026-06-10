package dentist.dentalhub.controller;

import dentist.dentalhub.model.Appointment;
import dentist.dentalhub.model.Patient;
import dentist.dentalhub.service.AppointmentService;
import dentist.dentalhub.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller to handle Patient module: registration, login, dashboard, and profile.
 */
@Controller
@RequestMapping("/patients")
public class PatientController {

    //  Fields are INSIDE the class
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    // Both services injected in constructor
    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    // --------------------- REGISTER ---------------------

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "register";
    }

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient, Model model) {
        if (patientService.getPatientByEmail(patient.getEmail()) != null) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }
        patientService.savePatient(patient);
        model.addAttribute("success", "Registration successful. Please login.");
        return "login";
    }

    // --------------------- LOGIN ---------------------

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "login";
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

        // Fetch appointments for this patient
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
        model.addAttribute("appointments", appointments);

        return "patient-dashboard";
    }

    // --------------------- APPOINTMENTS (ADDED) ---------------------

    /**
     * Handles the "Appointments" sidebar option & "Book New Appointment" CTA button.
     */
    @GetMapping("/appointments")
    public String patientAppointments(HttpSession session, Model model) {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        if (patient == null) return "redirect:/patients/login";

        model.addAttribute("patient", patient);

        // Pass appointments array to the booking view as well if needed
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
        model.addAttribute("appointments", appointments);

        return "patient-appointments"; // Make sure you have a 'patient-appointments.html' file!
    }

    // --------------------- PROFILE ---------------------

    @GetMapping("/profile")
    public String patientProfile(HttpSession session, Model model) {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        if (patient == null) return "redirect:/patients/login";

        model.addAttribute("patient", patient);

        // Pass appointments and counts for the stats card
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
        model.addAttribute("appointments", appointments);

        long upcoming = appointments.stream()
                .filter(a -> "CONFIRMED".equalsIgnoreCase(a.getStatus()) ||
                        "PENDING".equalsIgnoreCase(a.getStatus()))
                .count();

        long completed = appointments.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .count();

        model.addAttribute("upcomingCount", upcoming);
        model.addAttribute("completedCount", completed);

        return "patient-profile";
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

        model.addAttribute("patient", patient);
        model.addAttribute("success", "Profile updated successfully");

        // Re-add appointments for the stats card
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
        model.addAttribute("appointments", appointments);

        long upcoming = appointments.stream()
                .filter(a -> "CONFIRMED".equalsIgnoreCase(a.getStatus()) ||
                        "PENDING".equalsIgnoreCase(a.getStatus()))
                .count();
        long completed = appointments.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .count();

        model.addAttribute("upcomingCount", upcoming);
        model.addAttribute("completedCount", completed);

        return "patient-profile";
    }

    // --------------------- CHANGE PASSWORD ---------------------

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        Patient patient = (Patient) session.getAttribute("loggedInPatient");
        if (patient == null) return "redirect:/patients/login";

        // Re-add required model attributes regardless of outcome
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
        model.addAttribute("appointments", appointments);
        model.addAttribute("upcomingCount", appointments.stream()
                .filter(a -> "CONFIRMED".equalsIgnoreCase(a.getStatus()) ||
                        "PENDING".equalsIgnoreCase(a.getStatus())).count());
        model.addAttribute("completedCount", appointments.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus())).count());
        model.addAttribute("patient", patient);

        if (!patient.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "Current password is incorrect");
            return "patient-profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match");
            return "patient-profile";
        }

        patient.setPassword(newPassword);
        patientService.savePatient(patient);
        session.setAttribute("loggedInPatient", patient);

        model.addAttribute("success", "Password updated successfully");
        return "patient-profile";
    }


    // --------------------- LOGOUT ---------------------

    @GetMapping("/logout")
    public String logoutPatient(HttpSession session) {
        session.invalidate();
        return "redirect:/patients/login";
    }
}