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
    private final SubmissionRepository submissionRepository;
    private final HackathonRepository hackathonRepository;

    public ValuationServiceImpl(ValuationRepository valuationRepository,
                                SubmissionRepository submissionRepository,
                                HackathonRepository hackathonRepository) {
        this.valuationRepository = valuationRepository;
        this.submissionRepository = submissionRepository;
        this.hackathonRepository = hackathonRepository;
    }

    @Override
    public Valuation releaseValuation(Valuation valuation) {
        Hackathon hackathon = hackathonRepository
                .findById(valuation.getSubmission().getHackathon().getId())
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found"));

        hackathon.valuateSubmission(
                valuation.getSubmission().getTeam().getName(),
                valuation.getVote(),
                valuation.getJudgement()
        );

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