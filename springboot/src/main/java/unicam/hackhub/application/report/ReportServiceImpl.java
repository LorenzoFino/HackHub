package unicam.hackhub.application.report;

import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.command.CreateReportCommand;
import unicam.hackhub.application.dto.mapper.ReportResultMapper;
import unicam.hackhub.application.dto.response.ReportResult;
import unicam.hackhub.domain.exception.*;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of ReportService.
 * Orchestrates domain objects, repositories and exception handling.
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final TeamRepository teamRepository;
    private final ReportResultMapper mapper;

    public ReportServiceImpl(ReportRepository reportRepository,
                             HackathonRepository hackathonRepository,
                             StaffRepository staffRepository,
                             TeamRepository teamRepository,
                             ReportResultMapper mapper) {
        this.reportRepository = reportRepository;
        this.hackathonRepository = hackathonRepository;
        this.staffRepository = staffRepository;
        this.teamRepository = teamRepository;
        this.mapper = mapper;
    }

    @Override
    public ReportResult reportTeam(CreateReportCommand command) {
        Hackathon hackathon = hackathonRepository.findById(command.hackathonId())
                .orElseThrow(() -> new HackathonNotFoundException(command.hackathonId()));

        if (hackathon.getCurrentState() != HackathonStatus.StateType.PROGRESS)
            throw new InvalidHackathonStateException("Reports can only be filed during the PROGRESS state");

        Staff staff = staffRepository.findByEmail(command.mentorEmail())
                .orElseThrow(() -> new StaffNotFoundException(command.mentorEmail()));

        if (!(staff instanceof Mentor mentor))
            throw new InvalidHackathonStateException("Staff member is not a Mentor: " + command.mentorEmail());

        Team team = teamRepository.findById(command.teamName())
                .orElseThrow(() -> new TeamNotFoundException(command.teamName()));

        if (!hackathon.hasTeam(team))
            throw new InvalidHackathonStateException("Team is not registered to this hackathon");

        Report report = new Report(command.description(), LocalDate.now(), mentor, team, hackathon);
        return mapper.toResult(reportRepository.save(report));
    }

    @Override
    public void manageViolation(Long reportId, String status, boolean excludeTeam, Long hackathonId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));

        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new HackathonNotFoundException(hackathonId));

        if (hackathon.getCurrentState() != HackathonStatus.StateType.PROGRESS)
            throw new InvalidHackathonStateException("Violation can only be managed during the PROGRESS state");

        Report.ReportStatus newStatus;
        try {
            newStatus = Report.ReportStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidHackathonStateException("Invalid report status: " + status);
        }

        report.setStatus(newStatus);

        if (excludeTeam) {
            hackathon.unregisterTeam(report.getTeam());
            hackathonRepository.save(hackathon);
        }
        reportRepository.save(report);
    }

    @Override
    public List<ReportResult> findAllByHackathon(Long hackathonId) {
        return reportRepository.findAllByHackathonId(hackathonId)
                .stream().map(mapper::toResult).toList();
    }
}