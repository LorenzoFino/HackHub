package unicam.hackhub.application.support;

import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.command.CreateSupportRequestCommand;
import unicam.hackhub.domain.exception.*;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;
import unicam.hackhub.presentation.dto.mapper.SupportRequestMapper;
import unicam.hackhub.presentation.dto.response.SupportRequestResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of SupportRequestService.
 * Orchestrates domain objects, repositories and exception handling.
 */
@Service
public class SupportRequestServiceImpl implements SupportRequestService {

    private final SupportRequestRepository supportRequestRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final SupportRequestMapper mapper;

    public SupportRequestServiceImpl(SupportRequestRepository supportRequestRepository,
                                     HackathonRepository hackathonRepository,
                                     TeamRepository teamRepository,
                                     SupportRequestMapper mapper) {
        this.supportRequestRepository = supportRequestRepository;
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
        this.mapper = mapper;
    }

    @Override
    public SupportRequestResponse sendSupportRequest(CreateSupportRequestCommand command) {
        Team team = teamRepository.findById(command.teamName())
                .orElseThrow(() -> new TeamNotFoundException(command.teamName()));

        Hackathon hackathon = hackathonRepository.findById(command.hackathonId())
                .orElseThrow(() -> new HackathonNotFoundException(command.hackathonId()));

        if (!hackathon.hasTeam(team))
            throw new InvalidHackathonStateException("Team is not registered to this hackathon");

        SupportRequest request = new SupportRequest(command.description(), LocalDate.now(), team, hackathon);
        return mapper.toResponse(supportRequestRepository.save(request));
    }

    @Override
    public void cancelSupportRequest(Long supportRequestId) {
        SupportRequest request = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new SupportRequestNotFoundException(supportRequestId));

        if (request.getStatus() != SupportRequest.RequestStatus.PENDING)
            throw new IllegalStateException("Only pending requests can be cancelled");

        request.setStatus(SupportRequest.RequestStatus.CLOSED);
        supportRequestRepository.save(request);
    }

    @Override
    public List<SupportRequestResponse> findAllByHackathon(Long hackathonId) {
        return supportRequestRepository.findAllByHackathonId(hackathonId)
                .stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<SupportRequestResponse> findAllByTeam(String teamName) {
        return supportRequestRepository.findAllByTeamName(teamName)
                .stream().map(mapper::toResponse).toList();
    }

    @Override
    public SupportRequestResponse findById(Long id) {
        return mapper.toResponse(supportRequestRepository.findById(id)
                .orElseThrow(() -> new SupportRequestNotFoundException(id)));
    }
}