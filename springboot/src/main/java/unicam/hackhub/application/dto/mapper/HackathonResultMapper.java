package unicam.hackhub.application.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.response.HackathonResult;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Staff;
import unicam.hackhub.domain.model.Team;

@Component
public class HackathonResultMapper {
    public HackathonResult toResult(Hackathon h) {
        return new HackathonResult(
                h.getId(),
                h.getName(),
                h.getDescription(),
                h.getLocation(),
                h.getCurrentState(),
                h.getPrize(),
                h.getOrganizer().getEmail(),
                h.getJudge().getEmail(),
                h.getMentors().stream().map(Staff::getEmail).toList(),
                h.getRegisteredTeams().stream().map(Team::getName).toList(),
                h.getWinner() != null ? h.getWinner().getName() : null
        );
    }
}
