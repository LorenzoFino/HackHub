package unicam.hackhub.presentation.dto.mapper;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Report;
import unicam.hackhub.presentation.dto.response.ReportResponse;

@Component
public class ReportMapper {
    public ReportResponse toResponse(Report r) {
        return new ReportResponse(
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
