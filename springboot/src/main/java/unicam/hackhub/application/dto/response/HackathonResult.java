package unicam.hackhub.application.dto.response;

import unicam.hackhub.domain.model.HackathonStatus;
import java.time.LocalDate;
import java.util.List;

public record HackathonResult(
        Long id,
        String name,
        String description,
        String location,
        HackathonStatus.StateType currentState,
        Double prize,
        String organizerEmail,
        String judgeEmail,
        List<String> mentorEmails,
        List<String> registeredTeams,
        String winner
) {}
