package unicam.hackhub.application.submission;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.domain.model.Team;
import unicam.hackhub.domain.repository.HackathonRepository;
import unicam.hackhub.domain.repository.SubmissionRepository;
import unicam.hackhub.domain.repository.TeamRepository;

import java.util.List;

/**
 * Implementation of SubmissionService.
 * Orchestrates domain objects and repositories.
 */
@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 HackathonRepository hackathonRepository,
                                 TeamRepository teamRepository) {
        this.submissionRepository = submissionRepository;
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Submission sendSubmission(Submission submission) {
        Hackathon hackathon = hackathonRepository.findById(submission.getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        hackathon.addSubmission(submission.getTeam(), submission);
        hackathonRepository.save(hackathon);

        return submissionRepository.save(submission);
    }

    @Override
    public Submission updateSubmission(Long submissionId, Submission updatedSubmission) {
        Submission existing = submissionRepository.findByTeamAndHackathonId(
                        updatedSubmission.getTeam(),
                        updatedSubmission.getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));

        Hackathon hackathon = hackathonRepository.findById(existing.getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        hackathon.updateSubmission(existing.getTeam(), updatedSubmission);

        existing.setTitle(updatedSubmission.getTitle());
        existing.setDescription(updatedSubmission.getDescription());
        existing.setLink(updatedSubmission.getLink());

        return submissionRepository.save(existing);
    }

    @Override
    public List<Submission> findAllByHackathon(Long hackathonId) {
        return submissionRepository.findAllByHackathonId(hackathonId);
    }

    @Override
    public Submission findByTeamAndHackathon(String teamName, Long hackathonId) {
        Team team = teamRepository.findById(teamName)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamName));

        return submissionRepository.findByTeamAndHackathonId(team, hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));
    }
}