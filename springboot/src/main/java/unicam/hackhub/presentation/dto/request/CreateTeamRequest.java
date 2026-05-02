package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTeamRequest(
        @NotBlank String teamName,
        @NotBlank String creatorEmail
) {}
