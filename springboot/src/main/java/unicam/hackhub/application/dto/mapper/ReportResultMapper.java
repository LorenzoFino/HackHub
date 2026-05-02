package unicam.hackhub.application.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.application.dto.response.ReportResult;
import unicam.hackhub.domain.model.Report;

@Component
public class ReportResultMapper {
    public ReportResult toResult(Report r) {
        return new ReportResult(
                r.getId(),
                r.getDescription(),
                r.getDate(),
                r.getStatus().name(),
                r.getTeam().getName(),
                r.getHackathon().getId(),
                r.getMentor().getId()
        );
    }
}
