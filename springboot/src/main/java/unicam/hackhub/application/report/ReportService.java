package unicam.hackhub.application.report;

import unicam.hackhub.domain.model.Report;

import java.util.List;

/**
 * Application service for violation report management.
 * Covers the Mentor and Organizer use cases related to team violations.
 */
public interface ReportService {
    /**
     * Mentor reports a tteam forr a ruel violation
     * Only allowed when the hackathon is in PROGRESS state
     *
     * @param mentorEmail email of the mentor filing the report
     * @param teamName name of the team being reported
     * @param hackathonId id of the Hackathon
     * @param description description of the violation
     * @return the created Report
     */
    Report reportTeam(String mentorEmail, String teamName, Long hackathonId, String description);

    /**
     * Organizer manages a violation report, optionally excluding the team.
     * Only allowed when the hackathon is in PROGRESS state.
     *
     * @param reportId id of the report to manage
     * @param status new status for the report (REVIEWED or CLOSED)
     * @param excludeTeam if true, the team is unregistered from the hackathon
     * @param hackathonId id of the hackathon (used when excluding the team)
     */
    void manageViolation(Long reportId, String status, boolean excludeTeam, Long hackathonId);

    /**
     * Returns all reports for a given hackathon
     *
     * @param hackathonId id of the hackathon
     * @return list of reports
     */
    List<Report> findAllByHackathon(Long hackathonId);
}
