package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.User;
import unicam.hackhub.presentation.dto.response.UserResponse;

@Component
public class UserMapper {
    public UserResponse toResponse(User u) {
        return new UserResponse(
                u.getName(),
                u.getEmail(),
                u.getRole() != null ? u.getRole().name() : null,
                u.getTeam() != null ? u.getTeam().getName() : null
        );
    }
}
