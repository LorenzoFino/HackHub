package unicam.hackhub.application.team;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.HackathonRepository;
import unicam.hackhub.domain.repository.InvitationRepository;
import unicam.hackhub.domain.repository.TeamRepository;
import unicam.hackhub.domain.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of TeamService.
 * Orchestrates domain objects and repositories.
 */
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final HackathonRepository hackathonRepository;

    public TeamServiceImpl(TeamRepository teamRepository,
                           UserRepository userRepository,
                           InvitationRepository invitationRepository,
                           HackathonRepository hackathonRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.hackathonRepository = hackathonRepository;
    }

    @Override
    public Team createTeam(String teamName, String userEmail) {
        if (teamRepository.existsById(teamName))
            throw new IllegalArgumentException("Team name already taken: " + teamName);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));

        if (user.hasTeam())
            throw new IllegalStateException("User already belongs to a team");

        Team team = new Team(teamName, user);
        user.setTeam(team);

        teamRepository.save(team);
        userRepository.save(user);

        return team;
    }

    @Override
    public Invitation sendInvitation(String teamName, String recipientEmail) {
        Team team = findByName(teamName);

        if (hackathonRepository.existsByRegisteredTeams_Name(teamName))
            throw new IllegalStateException("Cannot invite members while the team is registered in a hackathon");

        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + recipientEmail));

        if (recipient.hasTeam())
            throw new IllegalStateException("User already belongs to a team");

        Invitation invitation = new Invitation(recipient, team, LocalDate.now());
        return invitationRepository.save(invitation);
    }

    @Override
    public void acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found: " + invitationId));

        if (invitation.getStatus() != Invitation.InvitationStatus.PENDING)
            throw new IllegalStateException("Invitation is no longer pending");

        User recipient = invitation.getRecipient();

        if (recipient.hasTeam())
            throw new IllegalStateException("User already belongs to a team");

        Team team = invitation.getTeam();

        recipient.setTeam(team);
        team.addMember(recipient);
        invitation.setStatus(Invitation.InvitationStatus.ACCEPTED);

        userRepository.save(recipient);
        teamRepository.save(invitation.getTeam());
        invitationRepository.save(invitation);
    }

    @Override
    public void declineInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found: " + invitationId));

        invitation.setStatus(Invitation.InvitationStatus.DECLINED);
        invitationRepository.save(invitation);
    }

    @Override
    public void leaveTeam(String teamName, String userEmail) {
        Team team = findByName(teamName);

        if (hackathonRepository.existsByRegisteredTeams_Name(teamName))
            throw new IllegalStateException("Cannot leave a team that is registered in a hackathon");

        if (team.isCreator(userEmail))
            throw new IllegalStateException("The creator cannot leave the team; use deleteTeam instead");

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));

        if (!team.getMembers().contains(user))
            throw new IllegalArgumentException("User is not a member of this team");

        team.removeMember(user);
        user.setTeam(null);

        userRepository.save(user);
        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(String teamName, String creatorEmail) {
        Team team = findByName(teamName);

        if (!team.isCreator(creatorEmail))
            throw new IllegalStateException("Only the creator can delete the team");

        if (hackathonRepository.existsByRegisteredTeams_Name(teamName))
            throw new IllegalStateException("Cannot delete a team that is registered in a hackathon");

        // Cancel all pending invitations for this team
        List<Invitation> pendingInvitations = invitationRepository
                .findAllByTeam_NameAndStatus(teamName, Invitation.InvitationStatus.PENDING);
        for (Invitation inv : pendingInvitations) {
            inv.setStatus(Invitation.InvitationStatus.DECLINED);
            invitationRepository.save(inv);
        }

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
        Team team = findByName(teamName);

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found: " + hackathonId));

        if (hackathon.getCurrentState() != HackathonStatus.StateType.SUBSCRIPTION)
            throw new IllegalStateException("Teams can only unregister during the SUBSCRIPTION state");

        hackathon.unregisterTeam(team);
        hackathonRepository.save(hackathon);
    }

    @Override
    public Team findByName(String teamName) {
        return teamRepository.findById(teamName)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamName));
    }
}