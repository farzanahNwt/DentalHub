package dentist.dentalhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DENTIST", schema = "E228142")
public class Dentist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DENTIST_ID")
    private Long dentistId;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "SPECIALIZATION")
    private String specialization;

    // ── Constructors ────────────────────────────────────────
    public Dentist() {}

    public Dentist(String fullName, String email, String password,
                   String phoneNumber, String specialization) {
        this.fullName       = fullName;
        this.email          = email;
        this.password       = password;
        this.phoneNumber    = phoneNumber;
        this.specialization = specialization;
    }

    // ── Getters & Setters ───────────────────────────────────
    public Long getDentistId()               { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }

    public String getFullName()              { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail()                 { return email; }
    public void setEmail(String email)       { this.email = email; }

    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber()                   { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber)   { this.phoneNumber = phoneNumber; }

    public String getSpecialization()                    { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}
