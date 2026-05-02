package unicam.hackhub.presentation.dto.response;

import java.time.LocalDate;

public record InvitationResponse(
        Long id,
        String status,
        LocalDate date,
        String teamName,
        String recipientEmail
) {}