package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Valuation;
import unicam.hackhub.presentation.dto.response.ValuationResponse;

@Component
public class ValuationMapper {
    public ValuationResponse toResponse(Valuation v) {
        return new ValuationResponse(
                v.getId(),
                v.getVote(),
                v.getJudgement(),
                v.getDate(),
                v.getSubmission().getId(),
                v.getJudge().getId()
        );
    }
}
