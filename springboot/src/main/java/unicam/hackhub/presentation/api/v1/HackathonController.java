package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.hackathon.HackathonService;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.StaffRepository;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * REST controller for hackathon management.
 * Covers the Organizer use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/hackathons")
public class HackathonController {

    private final HackathonService hackathonService;
    private final StaffRepository staffRepository;

    public HackathonController(HackathonService hackathonService, StaffRepository staffRepository) {
        this.hackathonService = hackathonService;
        this.staffRepository = staffRepository;
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
    public ResponseEntity<Hackathon> create(@RequestParam String name,
                                            @RequestParam String description,
                                            @RequestParam String rules,
                                            @RequestParam LocalDate registrationOpenDate,
                                            @RequestParam LocalDate registrationDeadline,
                                            @RequestParam LocalDate startDate,
                                            @RequestParam LocalDate endDate,
                                            @RequestParam String location,
                                            @RequestParam Integer maxTeamSize,
                                            @RequestParam Double prize,
                                            @RequestParam Long organizerId,
                                            @RequestParam Long judgeId,
                                            @RequestParam Long mentorId) {

        Organizer organizer = (Organizer) staffRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));
        Judge judge = (Judge) staffRepository.findById(judgeId)
                .orElseThrow(() -> new IllegalArgumentException("Judge not found"));
        Mentor mentor = (Mentor) staffRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found"));

        Hackathon hackathon = new Hackathon(
                name, description, rules,
                registrationOpenDate, registrationDeadline,
                new Period(startDate, endDate),
                location, maxTeamSize, prize,
                organizer, judge, Set.of(mentor)
        );

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

    /** POST /api/v1/hackathons/{id}/staff/{staffId}/role — assigns a role to a staff member */
    @PostMapping("/{id}/staff/{staffId}/ruolo")
    public ResponseEntity<RoleAssignment> roleAssign(@PathVariable Long id,
                                                          @PathVariable Long staffId,
                                                          @RequestParam String role) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hackathonService.roleAssign(id, staffId, role));
    }

    /** PUT /api/v1/hackathons/{id} — updates an existing hackathon (only during SUBSCRIPTION) */
    @PutMapping("/{id}")
    public ResponseEntity<Hackathon> update(@PathVariable Long id,
                                            @RequestBody Hackathon hackathon) {
        return ResponseEntity.ok(hackathonService.updateHackathon(id, hackathon));
    }

    /** DELETE /api/v1/hackathons/{id} — deletes a hackathon (only during SUBSCRIPTION) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hackathonService.deleteHackathon(id);
        return ResponseEntity.ok().build();
    }
}