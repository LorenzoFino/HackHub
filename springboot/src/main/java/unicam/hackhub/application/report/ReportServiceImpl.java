package unicam.hackhub.application.report;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.HackathonRepository;
import unicam.hackhub.domain.repository.ReportRepository;
import unicam.hackhub.domain.repository.StaffRepository;
import unicam.hackhub.domain.repository.TeamRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of ReportService
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final TeamRepository teamRepository;

    public ReportServiceImpl(ReportRepository reportRepository,
                             HackathonRepository hackathonRepository,
                             StaffRepository staffRepository,
                             TeamRepository teamRepository) {
        this.reportRepository = reportRepository;
        this.hackathonRepository = hackathonRepository;
        this.staffRepository = staffRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Report reportTeam(String mentorEmail, String teamName, Long hackathonId, String description) {
        Hackathon hackathon = findHackathon(hackathonId);

        if (hackathon.getCurrentState() != HackathonStatus.StateType.PROGRESS)
            throw new IllegalStateException("Reports can only be filed during the PROGRESS state");

        Staff staff = staffRepository.findByEmail(mentorEmail).orElseThrow(() -> new IllegalArgumentException("Mentor not found: " + mentorEmail));

        if (!(staff instanceof Mentor mentor))
            throw new IllegalStateException("Staff member is not a Mentor: " + mentorEmail);

        Team team = teamRepository.findById(teamName).orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamName));

        if (!hackathon.hasTeam(team))
            throw new IllegalStateException("Team is not registered to this hackathon");

        Report report = new Report(description, LocalDate.now(), mentor, team, hackathon);
        return reportRepository.save(report);
    }

    @Override
    public void manageViolation(Long reportId, String status, boolean excludeTeam, Long hackathonId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));

        Hackathon hackathon = findHackathon(hackathonId);

        if (hackathon.getCurrentState() != HackathonStatus.StateType.PROGRESS)
            throw new IllegalStateException("Violation can only be managed during the PROGRESS state");

        Report.ReportStatus newStatus;
        try {
            newStatus = Report.ReportStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid report status: " + status);
        }

        report.setStatus(newStatus);

        if (excludeTeam) {
            Team team = report.getTeam();
            hackathon.unregisterTeam(team);
            hackathonRepository.save(hackathon);
        }
        reportRepository.save(report);
    }

    @Override
    public List<Report> findAllByHackathon(Long hackathonId) {
        return reportRepository.findAllByHackathonId(hackathonId);
    }

    private Hackathon findHackathon(Long hackathonId) {
        return hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found: " + hackathonId));
    }
}
