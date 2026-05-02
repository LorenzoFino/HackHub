package unicam.hackhub.application.dto.command;

public record CreateTeamCommand(
        String teamName,
        String creatorEmail
) {}
