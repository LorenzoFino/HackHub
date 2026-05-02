package unicam.hackhub.application.report;

import unicam.hackhub.application.dto.command.CreateReportCommand;
import unicam.hackhub.application.dto.response.ReportResult;
import java.util.List;

public interface ReportService {

    /**
     * Mentor files a violation report for a team.
     * Only allowed when the hackathon is in PROGRESS state.
     *
     * @param command contains mentorEmail, teamName, hackathonId, description
     * @return the created ReportResult DTO
     */
    ReportResult reportTeam(CreateReportCommand command);

    /**
     * Organizer manages a violation report, optionally excluding the team.
     * Only allowed when the hackathon is in PROGRESS state.
     *
     * @param reportId    id of the report to manage
     * @param status      new status (REVIEWED or CLOSED)
     * @param excludeTeam if true, the team is unregistered from the hackathon
     * @param hackathonId id of the hackathon
     */
    void manageViolation(Long reportId, String status, boolean excludeTeam, Long hackathonId);

    /**
     * Returns all reports for a given hackathon.
     *
     * @param hackathonId id of the hackathon
     * @return list of ReportResult DTOs
     */
    List<ReportResult> findAllByHackathon(Long hackathonId);
}