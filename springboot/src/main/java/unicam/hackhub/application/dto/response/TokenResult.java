package unicam.hackhub.application.dto.response;

public record TokenResult(
        String token,
        String type,
        String email,
        String role
) {}