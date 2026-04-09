package unicam.hackhub.application.team;

import unicam.hackhub.domain.model.Invitation;
import unicam.hackhub.domain.model.Team;

/**
 * Application service for team management.
 * Covers the User use cases from the sequence diagrams.
 */
public interface TeamService {

    /** Creates a new team with the given user as first member */
    Team createTeam(String teamName, String userEmail);

    /** Sends an invitation to a user to join a team */
    Invitation sendInvitation(String teamName, String recipientEmail);

    /** Accepts an invitation — user joins the team */
    void acceptInvitation(Long invitationId);

    /** Declines an invitation */
    void declineInvitation(Long invitationId);

    /** Returns a team by name */
    Team findByName(String teamName);
}