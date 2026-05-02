package unicam.hackhub.application.dto.command;

public record RegisterCommand(
        String name,
        String email,
        String password
) {}
