package dentist.dentalhub.controller;

import dentist.dentalhub.model.Appointment;
import dentist.dentalhub.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/book")
    public String showBookingForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "booking";
    }

    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute("appointment") Appointment appointment) {
        appointmentService.bookAppointment(appointment);
        return "redirect:/appointments/history?patientId=" + appointment.getPatientId();
    }

    @GetMapping("/history")
    public String viewHistory(@RequestParam("patientId") Long patientId, Model model) {
        List<Appointment> history = appointmentService.getHistory(patientId);
        model.addAttribute("history", history);
        return "history";
    }
}