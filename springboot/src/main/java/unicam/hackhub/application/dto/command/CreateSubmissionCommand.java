package unicam.hackhub.application.dto.command;

public record CreateSubmissionCommand(
        String title,
        String description,
        String link,
        String teamName,
        Long hackathonId
) {}
