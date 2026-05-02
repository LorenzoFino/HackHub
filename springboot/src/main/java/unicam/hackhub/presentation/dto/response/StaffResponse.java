package unicam.hackhub.presentation.dto.response;

public record StaffResponse(
        Long id,
        String name,
        String surname,
        String email,
        String role
) {}
