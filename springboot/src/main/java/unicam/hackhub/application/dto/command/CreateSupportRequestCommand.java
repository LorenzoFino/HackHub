package unicam.hackhub.application.dto.command;

public record CreateSupportRequestCommand(
        String description,
        String teamName,
        Long hackathonId
) {}
