package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Team;
import unicam.hackhub.domain.model.User;
import unicam.hackhub.presentation.dto.response.TeamResponse;

@Component
public class TeamMapper {
    public TeamResponse toResponse(Team t) {
        return new TeamResponse(
                t.getName(),
                t.getCreator() != null ? t.getCreator().getEmail() : null,
                t.getMembers().stream().map(User::getEmail).toList(),
                t.getNumMembers(),
                t.getBalance(),
                t.getRegistrationDate()
        );
    }
}
