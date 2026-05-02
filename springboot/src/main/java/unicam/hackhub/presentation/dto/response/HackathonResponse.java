package unicam.hackhub.presentation.dto.response;

import unicam.hackhub.domain.model.HackathonStatus;
import java.time.LocalDate;
import java.util.List;

public record HackathonResponse(
        Long id,
        String name,
        String description,
        String rules,
        LocalDate registrationOpenDate,
        LocalDate registrationDeadline,
        LocalDate startDate,
        LocalDate endDate,
        String location,
        Integer maxTeamSize,
        Double prize,
        String organizerEmail,
        String judgeEmail,
        List<String> mentorEmails,
        List<String> registeredTeams,
        HackathonStatus.StateType currentState,
        String winner
) {}
