package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.hackathon.HackathonService;
import unicam.hackhub.domain.model.Hackathon;

import java.util.List;

/**
 * REST controller for hackathon management.
 * Covers the Organizer use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/hackathons")
public class HackathonController {

    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    /** GET /api/v1/hackathons — returns all hackathons */
    @GetMapping
    public ResponseEntity<List<Hackathon>> getAll() {
        return ResponseEntity.ok(hackathonService.findAll());
    }

    /** GET /api/v1/hackathons/{id} — returns a hackathon by id */
    @GetMapping("/{id}")
    public ResponseEntity<Hackathon> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hackathonService.findById(id));
    }

    /** POST /api/v1/hackathons — creates a new hackathon */
    @PostMapping
    public ResponseEntity<Hackathon> create(@RequestBody Hackathon hackathon) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hackathonService.createHackathon(hackathon));
    }

    /** POST /api/v1/hackathons/{id}/mentors/{mentorId} — adds a mentor */
    @PostMapping("/{id}/mentors/{mentorId}")
    public ResponseEntity<Void> addMentor(@PathVariable Long id,
                                          @PathVariable Long mentorId) {
        hackathonService.addMentor(id, mentorId);
        return ResponseEntity.ok().build();
    }

    /** POST /api/v1/hackathons/{id}/winner — declares the winning team */
    @PostMapping("/{id}/winner")
    public ResponseEntity<Void> declareWinner(@PathVariable Long id,
                                              @RequestParam String teamName) {
        hackathonService.declareWinner(id, teamName);
        return ResponseEntity.ok().build();
    }

    /** POST /api/v1/hackathons/{id}/next-state — advances to the next state */
    @PostMapping("/{id}/next-state")
    public ResponseEntity<Void> toNextState(@PathVariable Long id) {
        hackathonService.toNextState(id);
        return ResponseEntity.ok().build();
    }
}