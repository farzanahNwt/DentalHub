package dentist.dentalhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ADMIN", schema = "E228142")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_ID")
    private Long adminId;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    // ── Constructors ────────────────────────────────────────
    public Admin() {}

    public Admin(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email    = email;
        this.password = password;
    }

    // ── Getters & Setters ───────────────────────────────────
    public Long getAdminId()               { return adminId; }
    public void setAdminId(Long adminId)   { this.adminId = adminId; }

    public String getFullName()              { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail()                 { return email; }
    public void setEmail(String email)       { this.email = email; }

    public String getPassword()              { return password; }
    public void setPassword(String password) { this.password = password; }
}
