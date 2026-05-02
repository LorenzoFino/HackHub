package unicam.hackhub.presentation.dto.request;

public record LoginRequest(
        String email,
        String password
) {}
