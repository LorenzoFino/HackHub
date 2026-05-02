package unicam.hackhub.presentation.dto.response;

import java.time.LocalDate;

public record SubmissionResponse(
        Long id,
        String title,
        String description,
        String link,
        LocalDate submissionDate,
        String teamName,
        Long hackathonId,
        boolean evaluated
) {}