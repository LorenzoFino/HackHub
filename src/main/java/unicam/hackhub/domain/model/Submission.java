package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a submission sent by a team for a specific hackathon.
 *
 * Relationships (from Analysis_Diagram):
 * - sent by 1 Team
 * - assigned to 1 Valuation
 * - belongs to 1 Hackathon
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private LocalDate submissionDate;

    /** Team that sent this submission */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    /** Hackathon this submission belongs to */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    /** Valuation released by the judge — null until evaluated */
    @OneToOne(mappedBy = "submission", cascade = CascadeType.ALL)
    private Valuation valuation;

    public Submission(String title, String description, String link,
                      LocalDate submissionDate, Team team, Hackathon hackathon) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.submissionDate = submissionDate;
        this.team = team;
        this.hackathon = hackathon;
    }

    public boolean isEvaluated() {
        return this.valuation != null;
    }
}