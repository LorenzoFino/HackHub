package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Staff;
import unicam.hackhub.domain.model.Team;
import unicam.hackhub.presentation.dto.response.HackathonResponse;

@Component
public class HackathonMapper {
    public HackathonResponse toResponse(Hackathon h) {
        return new HackathonResponse(
                h.getId(),
                h.getName(),
                h.getDescription(),
                h.getRules(),
                h.getRegistrationOpenDate(),
                h.getRegistrationDeadline(),
                h.getPeriod().startDate(),
                h.getPeriod().endDate(),
                h.getLocation(),
                h.getMaxTeamSize(),
                h.getPrize(),
                h.getOrganizer().getEmail(),
                h.getJudge().getEmail(),
                h.getMentors().stream().map(Staff::getEmail).toList(),
                h.getRegisteredTeams().stream().map(Team::getName).toList(),
                h.getCurrentState(),
                h.getWinner() != null ? h.getWinner().getName() : null
        );
    }
}
