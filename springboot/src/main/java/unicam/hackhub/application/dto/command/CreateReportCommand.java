package unicam.hackhub.application.dto.command;

public record CreateReportCommand(
        String description,
        String teamName,
        Long hackathonId,
        String mentorEmail
) {}