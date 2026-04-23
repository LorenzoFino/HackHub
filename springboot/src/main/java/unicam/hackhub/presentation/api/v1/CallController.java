package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.call.CallService;
import unicam.hackhub.domain.model.MentorCall;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for mentor call management.
 * Covers the Mentor use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/calls")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    /** GET /api/v1/calls/{id} — returns a call by id */
    @GetMapping("/{id}")
    public ResponseEntity<MentorCall> getById(@PathVariable Long id) {
        return ResponseEntity.ok(callService.findById(id));
    }

    /** GET /api/v1/calls/support-request/{id} — returns all calls for a support request */
    @GetMapping("/support-request/{id}")
    public ResponseEntity<List<MentorCall>> getAllBySupportRequest(@PathVariable Long id) {
        return ResponseEntity.ok(callService.findAllBySupportRequest(id));
    }

    /** POST /api/v1/calls — proposes a new call */
    @PostMapping
    public ResponseEntity<MentorCall> propose(@RequestParam Long supportRequestId,
                                              @RequestParam LocalDate date,
                                              @RequestParam int duration) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(callService.proposeCall(supportRequestId, date, duration));
    }

    /** DELETE /api/v1/calls/{id} — cancels a call */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        callService.cancelCall(id);
        return ResponseEntity.ok().build();
    }
}