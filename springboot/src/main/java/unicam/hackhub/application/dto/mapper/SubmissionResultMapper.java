package unicam.hackhub.application.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.response.SubmissionResult;
import unicam.hackhub.domain.model.Submission;

@Component
public class SubmissionResultMapper {
    public SubmissionResult toResult(Submission s) {
        return new SubmissionResult(
                s.getId(),
                s.getTitle(),
                s.getDescription(),
                s.getLink(),
                s.getSubmissionDate(),
                s.getTeam().getName(),
                s.getHackathon().getId(),
                s.isEvaluated()
        );
    }
}