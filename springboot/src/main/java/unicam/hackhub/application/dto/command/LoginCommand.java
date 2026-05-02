package unicam.hackhub.application.dto.command;

public record LoginCommand(
        String email,
        String password
) {}
