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

@RestController
@RequestMapping("/api/v1/submissions")
public class UserController {

    private final SubmissionService submissionService;

    public UserController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<SubmissionResult>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(submissionService.findAllByHackathon(hackathonId));
    }

    @GetMapping("/hackathon/{hackathonId}/team/{teamName}")
    public ResponseEntity<SubmissionResult> getByTeamAndHackathon(@PathVariable Long hackathonId,
                                                                  @PathVariable String teamName) {
        return ResponseEntity.ok(submissionService.findByTeamAndHackathon(teamName, hackathonId));
    }

    @PostMapping
    public ResponseEntity<SubmissionResult> send(@Valid @RequestBody CreateSubmissionRequest request) {
        CreateSubmissionCommand command = new CreateSubmissionCommand(
                request.title(), request.description(), request.link(),
                request.teamName(), request.hackathonId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(submissionService.sendSubmission(command));
    }

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