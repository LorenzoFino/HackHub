package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.dto.command.CreateSubmissionCommand;
import unicam.hackhub.application.dto.response.SubmissionResult;
import unicam.hackhub.application.submission.SubmissionService;
import unicam.hackhub.presentation.dto.request.CreateSubmissionRequest;

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
    public ResponseEntity<List<SubmissionResult>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(submissionService.findAllByHackathon(hackathonId));
    }

    /** GET /api/v1/submissions/hackathon/{hackathonId}/team/{teamName} — returns a team submission */
    @GetMapping("/hackathon/{hackathonId}/team/{teamName}")
    public ResponseEntity<SubmissionResult> getByTeamAndHackathon(@PathVariable Long hackathonId,
                                                                  @PathVariable String teamName) {
        return ResponseEntity.ok(submissionService.findByTeamAndHackathon(teamName, hackathonId));
    }

    /** POST /api/v1/submissions — team member sends a new submission */
    @PostMapping
    public ResponseEntity<SubmissionResult> send(@Valid @RequestBody CreateSubmissionRequest request) {
        CreateSubmissionCommand command = new CreateSubmissionCommand(
                request.title(), request.description(), request.link(),
                request.teamName(), request.hackathonId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(submissionService.sendSubmission(command));
    }

    /** PUT /api/v1/submissions/{id} — team member updates an existing submission */
    @PutMapping("/{id}")
    public ResponseEntity<SubmissionResult> update(@PathVariable Long id,
                                                   @Valid @RequestBody CreateSubmissionRequest request) {
        CreateSubmissionCommand command = new CreateSubmissionCommand(
                request.title(), request.description(), request.link(),
                request.teamName(), request.hackathonId()
        );
        return ResponseEntity.ok(submissionService.updateSubmission(id, command));
    }
}