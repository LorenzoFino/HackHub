package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.team.TeamService;
import unicam.hackhub.domain.model.Invitation;
import unicam.hackhub.domain.model.Team;

/**
 * REST controller for team management.
 * Covers the User use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /** GET /api/v1/teams/{name} — returns a team by name */
    @GetMapping("/{name}")
    public ResponseEntity<Team> getByName(@PathVariable String name) {
        return ResponseEntity.ok(teamService.findByName(name));
    }

    /** POST /api/v1/teams — creates a new team */
    @PostMapping
    public ResponseEntity<Team> create(@RequestParam String teamName,
                                       @RequestParam String userEmail) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.createTeam(teamName, userEmail));
    }

    /** POST /api/v1/teams/{name}/invitations — sends an invitation */
    @PostMapping("/{name}/invitations")
    public ResponseEntity<Invitation> sendInvitation(@PathVariable String name,
                                                     @RequestParam String recipientEmail) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.sendInvitation(name, recipientEmail));
    }

    /** POST /api/v1/teams/invitations/{id}/accept — accepts an invitation */
    @PostMapping("/invitations/{id}/accept")
    public ResponseEntity<Void> acceptInvitation(@PathVariable Long id) {
        teamService.acceptInvitation(id);
        return ResponseEntity.ok().build();
    }

    /** POST /api/v1/teams/invitations/{id}/decline — declines an invitation */
    @PostMapping("/invitations/{id}/decline")
    public ResponseEntity<Void> declineInvitation(@PathVariable Long id) {
        teamService.declineInvitation(id);
        return ResponseEntity.ok().build();
    }
}