package unicam.hackhub.application.dto.command;

import java.time.LocalDate;

public record CreateHackathonCommand(
        String name,
        String description,
        String rules,
        LocalDate registrationOpenDate,
        LocalDate registrationDeadline,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        int maxTeamSize,
        double prize,
        Long organizerId,
        Long judgeId,
        Long mentorId
) {}
