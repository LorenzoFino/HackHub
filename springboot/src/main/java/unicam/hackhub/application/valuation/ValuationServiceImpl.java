package unicam.hackhub.application.valuation;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.domain.model.Valuation;
import unicam.hackhub.domain.repository.HackathonRepository;
import unicam.hackhub.domain.repository.SubmissionRepository;
import unicam.hackhub.domain.repository.ValuationRepository;

import java.util.List;

/**
 * Implementation of ValuationService.
 * Orchestrates domain objects and repositories.
 */
@Service
public class ValuationServiceImpl implements ValuationService {

    private final ValuationRepository valuationRepository;
    private final HackathonRepository hackathonRepository;
    private final SubmissionRepository submissionRepository;

    public ValuationServiceImpl(ValuationRepository valuationRepository,
                                HackathonRepository hackathonRepository,
                                SubmissionRepository submissionRepository) {
        this.valuationRepository = valuationRepository;
        this.hackathonRepository = hackathonRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Valuation releaseValuation(Valuation valuation) {
        // Load full submission from DB
        Submission submission = submissionRepository.findById(valuation.getSubmission().getId())
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));

        Hackathon hackathon = hackathonRepository.findById(submission.getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        hackathon.valuateSubmission(
                submission.getTeam().getName(),
                valuation.getVote(),
                valuation.getJudgement()
        );

        valuation.setSubmission(submission);
        valuation.setDate(java.time.LocalDate.now());

        hackathonRepository.save(hackathon);
        return valuationRepository.save(valuation);
    }

    @Override
    public Valuation updateValuation(Long valuationId, Valuation updatedValuation) {
        Valuation existing = valuationRepository.findById(valuationId)
                .orElseThrow(() -> new IllegalArgumentException("Valuation not found: " + valuationId));

        Hackathon hackathon = hackathonRepository
                .findById(existing.getSubmission().getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        hackathon.updateValuation(
                existing.getSubmission().getTeam().getName(),
                updatedValuation.getVote(),
                updatedValuation.getJudgement()
        );

        existing.setVote(updatedValuation.getVote());
        existing.setJudgement(updatedValuation.getJudgement());

        hackathonRepository.save(hackathon);
        return valuationRepository.save(existing);
    }

    @Override
    public List<Valuation> findAllByHackathon(Long hackathonId) {
        return valuationRepository.findAllBySubmissionHackathonId(hackathonId);
    }
}