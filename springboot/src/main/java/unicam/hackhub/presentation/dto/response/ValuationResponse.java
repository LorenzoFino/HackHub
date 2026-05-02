package unicam.hackhub.presentation.dto.response;

import java.time.LocalDate;

public record ValuationResponse(
        Long id,
        Integer vote,
        String judgement,
        LocalDate date,
        Long submissionId,
        Long judgeId
) {}
