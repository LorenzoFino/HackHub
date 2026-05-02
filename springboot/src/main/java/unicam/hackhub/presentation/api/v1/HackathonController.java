package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.dto.command.CreateHackathonCommand;
import unicam.hackhub.application.dto.command.UpdateHackathonCommand;
import unicam.hackhub.application.dto.response.HackathonResult;
import unicam.hackhub.application.hackathon.HackathonService;
import unicam.hackhub.domain.model.RoleAssignment;
import unicam.hackhub.presentation.dto.request.CreateHackathonRequest;
import unicam.hackhub.presentation.dto.request.UpdateHackathonRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hackathons")
public class HackathonController {

    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    @GetMapping
    public ResponseEntity<List<HackathonResult>> getAll() {
        return ResponseEntity.ok(hackathonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HackathonResult> getById(@PathVariable Long id) {
        return ResponseEntity.ok(hackathonService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HackathonResult> create(@Valid @RequestBody CreateHackathonRequest request) {
        CreateHackathonCommand command = new CreateHackathonCommand(
                request.name(), request.description(), request.rules(),
                request.registrationOpenDate(), request.registrationDeadline(),
                request.startDate(), request.endDate(), request.location(),
                request.maxTeamSize(), request.prize(),
                request.organizerId(), request.judgeId(), request.mentorId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(hackathonService.createHackathon(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HackathonResult> update(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateHackathonRequest request) {
        UpdateHackathonCommand command = new UpdateHackathonCommand(
                request.name(), request.description(), request.rules(),
                request.registrationOpenDate(), request.registrationDeadline(),
                request.startDate(), request.endDate(), request.location(),
                request.maxTeamSize(), request.prize()
        );
        return ResponseEntity.ok(hackathonService.updateHackathon(id, command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hackathonService.deleteHackathon(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/mentors/{mentorId}")
    public ResponseEntity<Void> addMentor(@PathVariable Long id,
                                          @PathVariable Long mentorId) {
        hackathonService.addMentor(id, mentorId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/winner")
    public ResponseEntity<Void> declareWinner(@PathVariable Long id,
                                              @RequestParam String teamName) {
        hackathonService.declareWinner(id, teamName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/next-state")
    public ResponseEntity<Void> toNextState(@PathVariable Long id) {
        hackathonService.toNextState(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/staff/{staffId}/ruolo")
    public ResponseEntity<RoleAssignment> roleAssign(@PathVariable Long id,
                                                     @PathVariable Long staffId,
                                                     @RequestParam String role) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hackathonService.roleAssign(id, staffId, role));
    }
}