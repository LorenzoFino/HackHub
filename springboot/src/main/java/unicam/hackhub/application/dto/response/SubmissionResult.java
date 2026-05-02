package unicam.hackhub.application.dto.response;

import java.time.LocalDate;

public record SubmissionResult(
        Long id,
        String title,
        String description,
        String link,
        LocalDate submissionDate,
        String teamName,
        Long hackathonId,
        boolean evaluated
) {}
