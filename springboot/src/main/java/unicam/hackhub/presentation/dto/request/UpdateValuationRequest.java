package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.*;

public record UpdateValuationRequest(
        @Min(0) @Max(10) int vote,
        @NotBlank String judgement
) {}
