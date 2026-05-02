package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.dto.command.CreateReportCommand;
import unicam.hackhub.application.dto.response.ReportResult;
import unicam.hackhub.application.report.ReportService;
import unicam.hackhub.presentation.dto.request.CreateReportRequest;

import java.util.List;

/**
 * REST controller for violation report management.
 * Covers the Mentor and Organizer use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /** GET /api/v1/reports/hackathon/{hackathonId} — returns all reports for a hackathon */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<ReportResult>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(reportService.findAllByHackathon(hackathonId));
    }

    /** POST /api/v1/reports — mentor files a violation report for a team */
    @PostMapping
    public ResponseEntity<ReportResult> reportTeam(@Valid @RequestBody CreateReportRequest request) {
        CreateReportCommand command = new CreateReportCommand(
                request.description(), request.teamName(),
                request.hackathonId(), request.mentorEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.reportTeam(command));
    }

    /** PUT /api/v1/reports/{id}/manage — organizer manages a violation report */
    @PutMapping("/{id}/manage")
    public ResponseEntity<Void> manageViolation(@PathVariable Long id,
                                                @RequestParam String status,
                                                @RequestParam(defaultValue = "false") boolean excludeTeam,
                                                @RequestParam Long hackathonId) {
        reportService.manageViolation(id, status, excludeTeam, hackathonId);
        return ResponseEntity.ok().build();
    }
}