package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.dto.command.CreateSupportRequestCommand;
import unicam.hackhub.application.support.SupportRequestService;
import unicam.hackhub.presentation.dto.request.CreateSupportRequestRequest;
import unicam.hackhub.presentation.dto.response.SupportRequestResponse;

import java.util.List;

/**
 * REST controller for support request management.
 * Covers the Team Member and Mentor use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/support-requests")
public class SupportRequestController {

    private final SupportRequestService supportRequestService;

    public SupportRequestController(SupportRequestService supportRequestService) {
        this.supportRequestService = supportRequestService;
    }

    /** GET /api/v1/support-requests/hackathon/{hackathonId} — returns all requests for a hackathon */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<SupportRequestResponse>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(supportRequestService.findAllByHackathon(hackathonId));
    }

    /** GET /api/v1/support-requests/team/{teamName} — returns all requests by a team */
    @GetMapping("/team/{teamName}")
    public ResponseEntity<List<SupportRequestResponse>> getAllByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(supportRequestService.findAllByTeam(teamName));
    }

    /** GET /api/v1/support-requests/{id} — returns a support request by id */
    @GetMapping("/{id}")
    public ResponseEntity<SupportRequestResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supportRequestService.findById(id));
    }

    /** POST /api/v1/support-requests — sends a new support request */
    @PostMapping
    public ResponseEntity<SupportRequestResponse> send(@Valid @RequestBody CreateSupportRequestRequest request) {
        CreateSupportRequestCommand command = new CreateSupportRequestCommand(
                request.description(), request.teamName(), request.hackathonId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supportRequestService.sendSupportRequest(command));
    }

    /** DELETE /api/v1/support-requests/{id} — cancels a support request */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        supportRequestService.cancelSupportRequest(id);
        return ResponseEntity.ok().build();
    }
}