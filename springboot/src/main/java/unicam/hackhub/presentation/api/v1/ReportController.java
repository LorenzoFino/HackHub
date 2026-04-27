package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.report.ReportService;
import unicam.hackhub.domain.model.Report;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /** GET /api/v1/reports/hackathon/{hackathonId} — returns all reports for a hackathon */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<Report>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(reportService.findAllByHackathon(hackathonId));
    }

    /**
     * POST /api/v1/reports — mentor files a violation report for a team.
     * Only allowed during PROGRESS state.
     */
    @PostMapping
    public ResponseEntity<Report> reportTeam(@RequestParam String emailMentor,
                                              @RequestParam String teamName,
                                              @RequestParam Long hackathonId,
                                              @RequestParam String description) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.reportTeam(emailMentor, teamName, hackathonId, description));
    }

    /**
     * PUT /api/v1/reports/{id}/manage — organizer manages a violation report.
     * Optionally excludes (unregisters) the team from the hackathon.
     */
    @PutMapping("/{id}/manage")
    public ResponseEntity<Void> manageViolation(@PathVariable Long id,
                                                   @RequestParam String status,
                                                   @RequestParam(defaultValue = "false") boolean excludeTeam,
                                                   @RequestParam Long hackathonId) {
        reportService.manageViolation(id, status, excludeTeam, hackathonId);
        return ResponseEntity.ok().build();
    }
}
