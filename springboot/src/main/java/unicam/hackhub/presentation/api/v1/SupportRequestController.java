package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.support.SupportRequestService;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.SupportRequest;
import unicam.hackhub.domain.model.Team;

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
    public ResponseEntity<List<SupportRequest>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(supportRequestService.findAllByHackathon(hackathonId));
    }

    /** GET /api/v1/support-requests/team/{teamName} — returns all requests by a team */
    @GetMapping("/team/{teamName}")
    public ResponseEntity<List<SupportRequest>> getAllByTeam(@PathVariable String teamName) {
        return ResponseEntity.ok(supportRequestService.findAllByTeam(teamName));
    }

    /** GET /api/v1/support-requests/{id} — returns a support request by id */
    @GetMapping("/{id}")
    public ResponseEntity<SupportRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supportRequestService.findById(id));
    }

    /** POST /api/v1/support-requests — sends a new support request */
    @PostMapping
    public ResponseEntity<SupportRequest> send(@RequestParam String teamName,
                                               @RequestParam Long hackathonId,
                                               @RequestParam String description) {
        SupportRequest request = new SupportRequest();
        request.setDescription(description);

        Team team = new Team();
        team.setName(teamName);
        request.setTeam(team);

        Hackathon hackathon = new Hackathon();
        hackathon.setId(hackathonId);
        request.setHackathon(hackathon);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supportRequestService.sendSupportRequest(request));
    }

    /** DELETE /api/v1/support-requests/{id} — cancels a support request */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        supportRequestService.cancelSupportRequest(id);
        return ResponseEntity.ok().build();
    }
}