package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.*;

public record CreateValuationRequest(
        @Min(0) @Max(10) int vote,
        @NotBlank String judgement,
        @NotNull Long submissionId,
        @NotNull Long judgeId
) {}
