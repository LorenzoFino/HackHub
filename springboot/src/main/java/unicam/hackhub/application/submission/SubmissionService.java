package unicam.hackhub.application.submission;

import unicam.hackhub.application.dto.command.CreateSubmissionCommand;
import unicam.hackhub.application.dto.response.SubmissionResult;
import java.util.List;

public interface SubmissionService {
    SubmissionResult sendSubmission(CreateSubmissionCommand command);
    SubmissionResult updateSubmission(Long submissionId, CreateSubmissionCommand command);
    List<SubmissionResult> findAllByHackathon(Long hackathonId);
    SubmissionResult findByTeamAndHackathon(String teamName, Long hackathonId);
}