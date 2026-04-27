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

    /** Sends an invitation to a user to join a team only if team is not registered in any hackathon */
    Invitation sendInvitation(String teamName, String recipientEmail);

    /** Accepts an invitation — user joins the team */
    void acceptInvitation(Long invitationId);

    /** Declines an invitation */
    void declineInvitation(Long invitationId);

    /**
     * A non-creator member leaves the team.
     * Only allowed if the team is not registered in any hackathon.
     */
    void leaveTeam(String teamName, String userEmail);

    /**
     * The creator deletes the team entirely.
     * Only allowed if the team is not registered in any hackathon.
     * Cancels all pending invitations and removes all members.
     */
    void deleteTeam(String teamName, String creatorEmail);

    /**
     * Unregisters the team from a hackathon.
     * Only allowed during the SUBSCRIPTION state.
     */
    void unregisterTeam(String teamName, Long hackathonId);

    /** Returns a team by name */
    Team findByName(String teamName);
}