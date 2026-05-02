package unicam.hackhub.application.team;

import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.command.CreateTeamCommand;
import unicam.hackhub.domain.exception.*;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;
import unicam.hackhub.presentation.dto.mapper.InvitationMapper;
import unicam.hackhub.presentation.dto.mapper.TeamMapper;
import unicam.hackhub.presentation.dto.response.InvitationResponse;
import unicam.hackhub.presentation.dto.response.TeamResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of TeamService.
 * Orchestrates domain objects, repositories and exception handling.
 */
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamMapper teamMapper;
    private final InvitationMapper invitationMapper;

    public TeamServiceImpl(TeamRepository teamRepository,
                           UserRepository userRepository,
                           InvitationRepository invitationRepository,
                           HackathonRepository hackathonRepository,
                           TeamMapper teamMapper,
                           InvitationMapper invitationMapper) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.hackathonRepository = hackathonRepository;
        this.teamMapper = teamMapper;
        this.invitationMapper = invitationMapper;
    }

    @Override
    public TeamResponse createTeam(CreateTeamCommand command) {
        if (teamRepository.existsById(command.teamName()))
            throw new IllegalArgumentException("Team name already taken: " + command.teamName());

        User user = userRepository.findByEmail(command.creatorEmail())
                .orElseThrow(() -> new UserNotFoundException(command.creatorEmail()));

        if (user.hasTeam())
            throw new UserAlreadyInTeamException(command.creatorEmail());

        Team team = new Team(command.teamName(), user);
        user.setTeam(team);
        teamRepository.save(team);
        userRepository.save(user);

        return teamMapper.toResponse(team);
    }

    @Override
    public InvitationResponse sendInvitation(String teamName, String recipientEmail) {
        Team team = getByName(teamName);

        if (hackathonRepository.existsByRegisteredTeams_Name(teamName))
            throw new IllegalStateException("Cannot invite members while the team is registered in a hackathon");

        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new UserNotFoundException(recipientEmail));

        if (recipient.hasTeam())
            throw new UserAlreadyInTeamException(recipientEmail);

        Invitation invitation = new Invitation(recipient, team, LocalDate.now());
        return invitationMapper.toResponse(invitationRepository.save(invitation));
    }

    @Override
    public void acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(invitationId));

        if (invitation.getStatus() != Invitation.InvitationStatus.PENDING)
            throw new IllegalStateException("Invitation is no longer pending");

        User recipient = invitation.getRecipient();
        if (recipient.hasTeam())
            throw new UserAlreadyInTeamException(recipient.getEmail());

        Team team = invitation.getTeam();
        recipient.setTeam(team);
        team.addMember(recipient);
        invitation.setStatus(Invitation.InvitationStatus.ACCEPTED);

        userRepository.save(recipient);
        teamRepository.save(team);
        invitationRepository.save(invitation);
    }

    @Override
    public void declineInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException(invitationId));
        invitation.setStatus(Invitation.InvitationStatus.DECLINED);
        invitationRepository.save(invitation);
    }

    @Override
    public void leaveTeam(String teamName, String userEmail) {
        Team team = getByName(teamName);

        if (hackathonRepository.existsByRegisteredTeams_Name(teamName))
            throw new IllegalStateException("Cannot leave a team that is registered in a hackathon");

        if (team.isCreator(userEmail))
            throw new IllegalStateException("The creator cannot leave the team; use deleteTeam instead");

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        if (!team.getMembers().contains(user))
            throw new IllegalArgumentException("User is not a member of this team");

        team.removeMember(user);
        user.setTeam(null);
        userRepository.save(user);
        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(String teamName, String creatorEmail) {
        Team team = getByName(teamName);

        if (!team.isCreator(creatorEmail))
            throw new IllegalStateException("Only the creator can delete the team");

        if (hackathonRepository.existsByRegisteredTeams_Name(teamName))
            throw new IllegalStateException("Cannot delete a team that is registered in a hackathon");

        // Cancel all invitations for this team regardless of status
        List<Invitation> allInvitations = invitationRepository.findAllByTeam_Name(teamName);
        allInvitations.forEach(invitationRepository::delete);

        // Detach all members from the team
        for (User member : team.getMembers()) {
            member.setTeam(null);
            userRepository.save(member);
        }
        team.getMembers().clear();
        teamRepository.save(team);
        teamRepository.delete(team);
    }

    @Override
    public void unregisterTeam(String teamName, Long hackathonId) {
        Team team = getByName(teamName);

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new HackathonNotFoundException(hackathonId));

        if (hackathon.getCurrentState() != HackathonStatus.StateType.SUBSCRIPTION)
            throw new InvalidHackathonStateException("Teams can only unregister during the SUBSCRIPTION state");

        hackathon.unregisterTeam(team);
        hackathonRepository.save(hackathon);
    }

    @Override
    public TeamResponse findByName(String teamName) {
        return teamMapper.toResponse(getByName(teamName));
    }

    /** Internal helper to load Team entity by name */
    private Team getByName(String teamName) {
        return teamRepository.findById(teamName)
                .orElseThrow(() -> new TeamNotFoundException(teamName));
    }
}