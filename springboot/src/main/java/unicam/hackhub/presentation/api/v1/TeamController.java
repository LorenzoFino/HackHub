package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.dto.command.CreateTeamCommand;
import unicam.hackhub.application.team.TeamService;
import unicam.hackhub.presentation.dto.request.CreateTeamRequest;
import unicam.hackhub.presentation.dto.response.InvitationResponse;
import unicam.hackhub.presentation.dto.response.TeamResponse;

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
    public ResponseEntity<TeamResponse> getByName(@PathVariable String name) {
        return ResponseEntity.ok(teamService.findByName(name));
    }

    /** POST /api/v1/teams — creates a new team */
    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody CreateTeamRequest request) {
        CreateTeamCommand command = new CreateTeamCommand(request.teamName(), request.creatorEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamService.createTeam(command));
    }

    /** POST /api/v1/teams/{name}/invitations — sends an invitation to a user */
    @PostMapping("/{name}/invitations")
    public ResponseEntity<InvitationResponse> sendInvitation(@PathVariable String name,
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

    /** POST /api/v1/teams/{name}/leave — a non-creator member leaves the team */
    @PostMapping("/{name}/leave")
    public ResponseEntity<Void> leaveTeam(@PathVariable String name,
                                          @RequestParam String userEmail) {
        teamService.leaveTeam(name, userEmail);
        return ResponseEntity.ok().build();
    }

    /** DELETE /api/v1/teams/{name} — creator deletes the team */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String name,
                                           @RequestParam String creatorEmail) {
        teamService.deleteTeam(name, creatorEmail);
        return ResponseEntity.noContent().build();
    }

    /** DELETE /api/v1/teams/{name}/hackathons/{hackathonId} — unregisters team from hackathon */
    @DeleteMapping("/{name}/hackathons/{hackathonId}")
    public ResponseEntity<Void> unregisterTeam(@PathVariable String name,
                                               @PathVariable Long hackathonId) {
        teamService.unregisterTeam(name, hackathonId);
        return ResponseEntity.ok().build();
    }
}