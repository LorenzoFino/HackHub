package unicam.hackhub.application.dto.command;

public record UpdateValuationCommand(
        int vote,
        String judgement
) {}
