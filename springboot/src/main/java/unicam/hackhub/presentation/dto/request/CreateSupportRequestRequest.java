package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSupportRequestRequest(
        @NotBlank String description,
        @NotBlank String teamName,
        @NotNull Long hackathonId
) {}
