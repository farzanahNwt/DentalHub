package dentist.dentalhub.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "dentist_id", nullable = false)
    private Long dentistId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", length = 10, nullable = false)
    private String appointmentTime;

    @Column(name = "treatment_type", length = 50, nullable = false)
    private String treatmentType;

    @Column(name = "status", length = 20, nullable = false)
    private String status = "PENDING";

    @Column(name = "notes")
    private String notes;
}