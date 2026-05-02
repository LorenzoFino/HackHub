package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.presentation.dto.response.SubmissionResponse;

@Component
public class SubmissionMapper {
    public SubmissionResponse toResponse(Submission s) {
        return new SubmissionResponse(
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
