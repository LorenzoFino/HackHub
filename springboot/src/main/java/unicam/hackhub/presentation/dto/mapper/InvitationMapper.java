package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Invitation;
import unicam.hackhub.presentation.dto.response.InvitationResponse;

@Component
public class InvitationMapper {
    public InvitationResponse toResponse(Invitation i) {
        return new InvitationResponse(
                i.getId(),
                i.getStatus().name(),
                i.getDate(),
                i.getTeam().getName(),
                i.getRecipient().getEmail()
        );
    }
}
