package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.*;

public record UpdateValuationRequest(
        @Min(0) @Max(10) Integer vote,
        @NotBlank String judgement
) {}
