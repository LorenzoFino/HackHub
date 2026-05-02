package unicam.hackhub.presentation.dto.response;

import java.time.LocalDate;

public record SupportRequestResponse(
        Long id,
        String description,
        LocalDate date,
        String status,
        String teamName,
        Long hackathonId
) {}
