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

    // Matches booking.html
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

    // FIXED: Changed from "history" to match "appointment-history" in your templates folder
    @GetMapping("/history")
    public String viewHistory(@RequestParam("patientId") Long patientId, Model model) {
        List<Appointment> history = appointmentService.getHistory(patientId);
        model.addAttribute("history", history);
        return "appointment-history";
    }

    // Matches appointment-details.html (If you create it later for your task list)
    @GetMapping("/details")
    public String viewDetails(@RequestParam("id") Long appointmentId, Model model) {
        appointmentService.getAppointmentDetails(appointmentId).ifPresent(appt -> model.addAttribute("appointment", appt));
        return "appointment-details";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam("id") Long appointmentId, @RequestParam("patientId") Long patientId) {
        appointmentService.cancelAppointment(appointmentId);
        return "redirect:/appointments/history?patientId=" + patientId;
    }
}