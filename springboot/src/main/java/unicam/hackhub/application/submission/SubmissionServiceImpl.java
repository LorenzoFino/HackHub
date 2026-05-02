package unicam.hackhub.application.submission;

import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.command.CreateSubmissionCommand;
import unicam.hackhub.application.dto.mapper.SubmissionResultMapper;
import unicam.hackhub.application.dto.response.SubmissionResult;
import unicam.hackhub.domain.exception.*;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final SubmissionResultMapper mapper;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 HackathonRepository hackathonRepository,
                                 TeamRepository teamRepository,
                                 SubmissionResultMapper mapper) {
        this.submissionRepository = submissionRepository;
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
        this.mapper = mapper;
    }

    @Override
    public SubmissionResult sendSubmission(CreateSubmissionCommand command) {
        Hackathon hackathon = hackathonRepository.findById(command.hackathonId())
                .orElseThrow(() -> new HackathonNotFoundException(command.hackathonId()));

        Team team = teamRepository.findById(command.teamName())
                .orElseThrow(() -> new TeamNotFoundException(command.teamName()));

        Submission submission = new Submission(
                command.title(), command.description(), command.link(),
                LocalDate.now(), team, hackathon
        );

        hackathon.addSubmission(team, submission);
        hackathonRepository.save(hackathon);

        return mapper.toResult(submissionRepository.save(submission));
    }

    @Override
    public SubmissionResult updateSubmission(Long submissionId, CreateSubmissionCommand command) {
        Team team = teamRepository.findById(command.teamName())
                .orElseThrow(() -> new TeamNotFoundException(command.teamName()));

        Submission existing = submissionRepository.findByTeamAndHackathonId(team, command.hackathonId())
                .orElseThrow(() -> new SubmissionNotFoundException(submissionId));

        Hackathon hackathon = hackathonRepository.findById(existing.getHackathon().getId())
                .orElseThrow(() -> new HackathonNotFoundException(existing.getHackathon().getId()));

        hackathon.updateSubmission(team, existing);
        existing.setTitle(command.title());
        existing.setDescription(command.description());
        existing.setLink(command.link());

        return mapper.toResult(submissionRepository.save(existing));
    }

    @Override
    public List<SubmissionResult> findAllByHackathon(Long hackathonId) {
        return submissionRepository.findAllByHackathonId(hackathonId)
                .stream().map(mapper::toResult).toList();
    }

    @Override
    public SubmissionResult findByTeamAndHackathon(String teamName, Long hackathonId) {
        Team team = teamRepository.findById(teamName)
                .orElseThrow(() -> new TeamNotFoundException(teamName));
        return mapper.toResult(
                submissionRepository.findByTeamAndHackathonId(team, hackathonId)
                        .orElseThrow(() -> new SubmissionNotFoundException(hackathonId))
        );
    }
}