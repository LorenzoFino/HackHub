package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents an invitation sent by a team member to another user.
 * A user can only belong to one team at a time.
 *
 * Relationships (from Analysis_Diagram):
 * - sent to 1 User
 * - sent from 1 Team
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "invitation")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(nullable = false)
    private LocalDate date;

    /** User who received the invitation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    private User recipient;

    /** Team that sent the invitation */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name", nullable = false)
    private Team team;

    public Invitation(User recipient, Team team, LocalDate date) {
        this.recipient = recipient;
        this.team = team;
        this.date = date;
        this.status = InvitationStatus.PENDING;
    }

    public enum InvitationStatus {
        PENDING,   // waiting for user response
        ACCEPTED,  // user joined the team
        DECLINED   // user declined the invitation
    }
}