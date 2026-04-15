package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.submission.SubmissionService;
import unicam.hackhub.domain.model.Submission;

import java.util.List;

/**
 * REST controller for submission management.
 * Covers the Team Member use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/submissions")
public class UserController {

    private final SubmissionService submissionService;

    public UserController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /** GET /api/v1/submissions/hackathon/{hackathonId} — returns all submissions for a hackathon */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<Submission>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(submissionService.findAllByHackathon(hackathonId));
    }

    /** GET /api/v1/submissions/hackathon/{hackathonId}/team/{teamName} — returns a team submission */
    @GetMapping("/hackathon/{hackathonId}/team/{teamName}")
    public ResponseEntity<Submission> getByTeamAndHackathon(@PathVariable Long hackathonId,
                                                            @PathVariable String teamName) {
        return ResponseEntity.ok(submissionService.findByTeamAndHackathon(teamName, hackathonId));
    }

    /** POST /api/v1/submissions — sends a new submission */
    @PostMapping
    public ResponseEntity<Submission> send(@RequestBody Submission submission) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(submissionService.sendSubmission(submission));
    }

    /** PUT /api/v1/submissions/{id} — updates an existing submission */
    @PutMapping("/{id}")
    public ResponseEntity<Submission> update(@PathVariable Long id,
                                             @RequestBody Submission submission) {
        return ResponseEntity.ok(submissionService.updateSubmission(id, submission));
    }
}