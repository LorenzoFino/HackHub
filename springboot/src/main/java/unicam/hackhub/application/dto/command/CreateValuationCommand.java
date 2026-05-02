package unicam.hackhub.application.dto.command;

public record CreateValuationCommand(
        int vote,
        String judgement,
        Long submissionId,
        Long judgeId
) {}
