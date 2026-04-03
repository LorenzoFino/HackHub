package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Call;

import java.time.LocalDate;

/**
 * Represents a support request sent by a team to a mentor.
 *
 * Relationships (from Analysis_Diagram):
 * - sent by 1 Team
 * - can generate 0..1 Call (if mentor proposes one)
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "support_request")
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    /** Team that sent this support request */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    /** Hackathon this support request belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    /** Call generated from this request — null until mentor proposes one */
    @OneToOne(mappedBy = "supportRequest", cascade = CascadeType.ALL)
    private MentorCall call;

    public SupportRequest(String description, LocalDate date, Team team, Hackathon hackathon) {
        this.description = description;
        this.date = date;
        this.team = team;
        this.hackathon = hackathon;
        this.status = RequestStatus.PENDING;
    }

    public enum RequestStatus {
        PENDING,   // waiting for mentor response
        ACCEPTED,  // mentor accepted and proposed a call
        CLOSED     // request resolved
    }
}