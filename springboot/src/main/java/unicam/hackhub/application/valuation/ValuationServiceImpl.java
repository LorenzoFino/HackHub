package unicam.hackhub.application.valuation;

import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.command.CreateValuationCommand;
import unicam.hackhub.application.dto.command.UpdateValuationCommand;
import unicam.hackhub.application.dto.mapper.ValuationResultMapper;
import unicam.hackhub.application.dto.response.ValuationResult;
import unicam.hackhub.domain.exception.*;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;

import java.time.LocalDate;
import java.util.List;

@Service
public class ValuationServiceImpl implements ValuationService {

    private final ValuationRepository valuationRepository;
    private final HackathonRepository hackathonRepository;
    private final SubmissionRepository submissionRepository;
    private final StaffRepository staffRepository;
    private final ValuationResultMapper mapper;

    public ValuationServiceImpl(ValuationRepository valuationRepository,
                                HackathonRepository hackathonRepository,
                                SubmissionRepository submissionRepository,
                                StaffRepository staffRepository,
                                ValuationResultMapper mapper) {
        this.valuationRepository = valuationRepository;
        this.hackathonRepository = hackathonRepository;
        this.submissionRepository = submissionRepository;
        this.staffRepository = staffRepository;
        this.mapper = mapper;
    }

    @Override
    public ValuationResult releaseValuation(CreateValuationCommand command) {
        Submission submission = submissionRepository.findById(command.submissionId())
                .orElseThrow(() -> new SubmissionNotFoundException(command.submissionId()));

        Hackathon hackathon = hackathonRepository.findById(submission.getHackathon().getId())
                .orElseThrow(() -> new HackathonNotFoundException(submission.getHackathon().getId()));

        Judge judge = (Judge) staffRepository.findById(command.judgeId())
                .orElseThrow(() -> new StaffNotFoundException(command.judgeId()));

        hackathon.valuateSubmission(
                submission.getTeam().getName(),
                command.vote(),
                command.judgement()
        );

        Valuation valuation = new Valuation(
                command.vote(), command.judgement(),
                LocalDate.now(), submission, judge
        );

        hackathonRepository.save(hackathon);
        return mapper.toResult(valuationRepository.save(valuation));
    }

    @Override
    public ValuationResult updateValuation(Long valuationId, UpdateValuationCommand command) {
        Valuation existing = valuationRepository.findById(valuationId)
                .orElseThrow(() -> new ValuationNotFoundException(valuationId));

        Hackathon hackathon = hackathonRepository
                .findById(existing.getSubmission().getHackathon().getId())
                .orElseThrow(() -> new HackathonNotFoundException(
                        existing.getSubmission().getHackathon().getId()));

        hackathon.updateValuation(
                existing.getSubmission().getTeam().getName(),
                command.vote(),
                command.judgement()
        );

        existing.setVote(command.vote());
        existing.setJudgement(command.judgement());

        hackathonRepository.save(hackathon);
        return mapper.toResult(valuationRepository.save(existing));
    }

    @Override
    public List<ValuationResult> findAllByHackathon(Long hackathonId) {
        return valuationRepository.findAllBySubmissionHackathonId(hackathonId)
                .stream().map(mapper::toResult).toList();
    }
}