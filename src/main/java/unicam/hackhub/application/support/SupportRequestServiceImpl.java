package unicam.hackhub.application.support;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.SupportRequest;
import unicam.hackhub.domain.model.Team;
import unicam.hackhub.domain.repository.HackathonRepository;
import unicam.hackhub.domain.repository.SupportRequestRepository;
import unicam.hackhub.domain.repository.TeamRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of SupportRequestService.
 * Orchestrates domain objects and repositories.
 */
@Service
public class SupportRequestServiceImpl implements SupportRequestService {

    private final SupportRequestRepository supportRequestRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;

    public SupportRequestServiceImpl(SupportRequestRepository supportRequestRepository,
                                     HackathonRepository hackathonRepository,
                                     TeamRepository teamRepository) {
        this.supportRequestRepository = supportRequestRepository;
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public SupportRequest sendSupportRequest(SupportRequest supportRequest) {
        Team team = teamRepository.findById(supportRequest.getTeam().getName())
                .orElseThrow(() -> new IllegalArgumentException("Team not found"));

        Hackathon hackathon = hackathonRepository
                .findById(supportRequest.getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        if (!hackathon.hasTeam(team))
            throw new IllegalStateException("Team is not registered to this hackathon");

        SupportRequest newRequest = new SupportRequest(
                supportRequest.getDescription(),
                LocalDate.now(),
                team,
                hackathon
        );

        return supportRequestRepository.save(newRequest);
    }

    @Override
    public void cancelSupportRequest(Long supportRequestId) {
        SupportRequest supportRequest = findById(supportRequestId);

        if (supportRequest.getStatus() != SupportRequest.RequestStatus.PENDING)
            throw new IllegalStateException("Only pending requests can be cancelled");

        supportRequest.setStatus(SupportRequest.RequestStatus.CLOSED);
        supportRequestRepository.save(supportRequest);
    }

    @Override
    public List<SupportRequest> findAllByHackathon(Long hackathonId) {
        return supportRequestRepository.findAllByHackathonId(hackathonId);
    }

    @Override
    public List<SupportRequest> findAllByTeam(String teamName) {
        return supportRequestRepository.findAllByTeamName(teamName);
    }

    @Override
    public SupportRequest findById(Long id) {
        return supportRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Support request not found: " + id));
    }
}