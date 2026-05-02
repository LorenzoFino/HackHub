package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSubmissionRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String link,
        @NotBlank String teamName,
        @NotNull Long hackathonId
) {}
