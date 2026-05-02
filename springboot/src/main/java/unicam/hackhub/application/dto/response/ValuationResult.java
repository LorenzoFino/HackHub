package unicam.hackhub.application.dto.response;

import java.time.LocalDate;

public record ValuationResult(
        Long id,
        Integer vote,
        String judgement,
        LocalDate date,
        Long submissionId,
        Long judgeId
) {}
