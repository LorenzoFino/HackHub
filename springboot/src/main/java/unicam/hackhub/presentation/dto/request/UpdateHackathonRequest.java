package unicam.hackhub.presentation.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdateHackathonRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String rules,
        @NotNull LocalDate registrationOpenDate,
        @NotNull LocalDate registrationDeadline,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotBlank String location,
        @Min(1) @Max(20) Integer maxTeamSize,
        @PositiveOrZero double prize
) {}
