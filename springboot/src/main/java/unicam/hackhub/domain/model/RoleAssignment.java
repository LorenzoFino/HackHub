package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Tracks which Staff member holds which role in which Hackathon.
 * A staff member can have different roles across different hackathons.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "role_assignment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"staff_id", "hackathon_id"}))
public class RoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private LocalDate assignmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    public RoleAssignment(String role, Staff staff, Hackathon hackathon) {
        this.role = role;
        this.staff = staff;
        this.hackathon = hackathon;
        this.assignmentDate = LocalDate.now();
    }
}
