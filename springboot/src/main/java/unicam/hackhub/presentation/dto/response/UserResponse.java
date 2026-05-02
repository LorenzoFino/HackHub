package unicam.hackhub.presentation.dto.response;

public record UserResponse(
        String name,
        String email,
        String role,
        String teamName
) {}
