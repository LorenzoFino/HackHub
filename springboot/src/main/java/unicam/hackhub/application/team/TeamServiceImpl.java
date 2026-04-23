package unicam.hackhub.application.team;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.Invitation;
import unicam.hackhub.domain.model.Team;
import unicam.hackhub.domain.model.User;
import unicam.hackhub.domain.repository.InvitationRepository;
import unicam.hackhub.domain.repository.TeamRepository;
import unicam.hackhub.domain.repository.UserRepository;

import java.time.LocalDate;

/**
 * Implementation of TeamService.
 * Orchestrates domain objects and repositories.
 */
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;

    public TeamServiceImpl(TeamRepository teamRepository,
                           UserRepository userRepository,
                           InvitationRepository invitationRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
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

        User recipient = invitation.getRecipient();

        if (recipient.hasTeam())
            throw new IllegalStateException("User already belongs to a team");

        recipient.setTeam(invitation.getTeam());
        invitation.getTeam().addMember(recipient);
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
    public Team findByName(String teamName) {
        return teamRepository.findById(teamName)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamName));
    }
}