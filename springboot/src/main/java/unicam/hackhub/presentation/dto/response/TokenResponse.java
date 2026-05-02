package unicam.hackhub.presentation.dto.response;

public record TokenResponse(
        String token,
        String type,
        String email,
        String role
) {}
