package unicam.hackhub.application.team;

import unicam.hackhub.application.dto.command.CreateTeamCommand;
import unicam.hackhub.application.dto.response.*;
import unicam.hackhub.presentation.dto.response.InvitationResponse;
import unicam.hackhub.presentation.dto.response.TeamResponse;

public interface TeamService {

    /**
     * Creates a new team with the given user as creator and first member.
     *
     * @param command contains teamName and creatorEmail
     * @return the created TeamResponse DTO
     */
    TeamResponse createTeam(CreateTeamCommand command);

    /**
     * Sends an invitation to a user to join a team.
     * Only allowed if the team is not registered in any hackathon.
     *
     * @param teamName       name of the team
     * @param recipientEmail email of the user to invite
     * @return the created InvitationResponse DTO
     */
    InvitationResponse sendInvitation(String teamName, String recipientEmail);

    /**
     * Accepts a pending invitation — the user joins the team.
     *
     * @param invitationId id of the invitation to accept
     */
    void acceptInvitation(Long invitationId);

    /**
     * Declines a pending invitation.
     *
     * @param invitationId id of the invitation to decline
     */
    void declineInvitation(Long invitationId);

    /**
     * A non-creator member leaves the team.
     * Only allowed if the team is not registered in any hackathon.
     *
     * @param teamName  name of the team
     * @param userEmail email of the user leaving
     */
    void leaveTeam(String teamName, String userEmail);

    /**
     * The creator deletes the team entirely.
     * Cancels all invitations and removes all members.
     * Only allowed if the team is not registered in any hackathon.
     *
     * @param teamName     name of the team
     * @param creatorEmail email of the creator
     */
    void deleteTeam(String teamName, String creatorEmail);

    /**
     * Unregisters the team from a hackathon.
     * Only allowed during the SUBSCRIPTION state.
     *
     * @param teamName    name of the team
     * @param hackathonId id of the hackathon
     */
    void unregisterTeam(String teamName, Long hackathonId);

    /**
     * Returns a team by name.
     *
     * @param teamName name of the team
     * @return the TeamResponse DTO
     */
    TeamResponse findByName(String teamName);
}