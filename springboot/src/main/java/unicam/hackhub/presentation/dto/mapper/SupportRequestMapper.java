package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.SupportRequest;
import unicam.hackhub.presentation.dto.response.SupportRequestResponse;

@Component
public class SupportRequestMapper {
    public SupportRequestResponse toResponse(SupportRequest s) {
        return new SupportRequestResponse(
                s.getId(),
                s.getDescription(),
                s.getDate(),
                s.getStatus().name(),
                s.getTeam().getName(),
                s.getHackathon().getId()
        );
    }
}
