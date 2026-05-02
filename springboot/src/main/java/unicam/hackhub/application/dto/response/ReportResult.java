package unicam.hackhub.application.dto.response;

import java.time.LocalDate;

public record ReportResult(
        Long id,
        String description,
        LocalDate date,
        String status,
        String teamName,
        Long hackathonId,
        Long mentorId
) {}
