package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a valuation released by a judge for a team submission.
 *
 * Relationships (from Analysis_Diagram):
 * - assigned to 1 Submission
 * - released by 1 Judge
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "valuation")
public class Valuation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer vote;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String judgement;

    @Column(nullable = false)
    private LocalDate date;

    /** Submission this valuation refers to */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    /** Judge who released this valuation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judge_id", nullable = false)
    private Judge judge;

    public Valuation(Integer vote, String judgement, LocalDate date,
                     Submission submission, Judge judge) {
        this.vote = vote;
        this.judgement = judgement;
        this.date = date;
        this.submission = submission;
        this.judge = judge;
    }
}