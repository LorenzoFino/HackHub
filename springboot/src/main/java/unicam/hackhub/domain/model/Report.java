package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a violation report sent by a mentor to the organizer.
 * Created when a mentor notices a team breaking the hackathon rules.
 *
 * Relationships (from Analysis_Diagram):
 * - sent by 1 Mentor
 * - refers to 1 Team
 * - belongs to 1 Hackathon
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    /** Mentor who filed the report */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    /** Team being reported */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    /** Hackathon this report belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    public Report(String description, LocalDate date,
                  Mentor mentor, Team team, Hackathon hackathon) {
        this.description = description;
        this.date = date;
        this.mentor = mentor;
        this.team = team;
        this.hackathon = hackathon;
        this.status = ReportStatus.PENDING;
    }

    public enum ReportStatus {
        PENDING,   // waiting for organizer decision
        REVIEWED,  // organizer has reviewed the report
        CLOSED     // report resolved
    }
}