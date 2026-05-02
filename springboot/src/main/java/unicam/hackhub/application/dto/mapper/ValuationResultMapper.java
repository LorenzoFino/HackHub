package unicam.hackhub.application.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.response.ValuationResult;
import unicam.hackhub.domain.model.Valuation;

@Component
public class ValuationResultMapper {
    public ValuationResult toResult(Valuation v) {
        return new ValuationResult(
                v.getId(),
                v.getVote(),
                v.getJudgement(),
                v.getDate(),
                v.getSubmission().getId(),
                v.getJudge().getId()
        );
    }
}
