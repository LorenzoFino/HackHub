package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.call.CallService;
import unicam.hackhub.presentation.dto.response.SupportRequestResponse;

import java.time.LocalDate;

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

    /** POST /api/v1/calls — proposes a new call to a team */
    @PostMapping
    public ResponseEntity<SupportRequestResponse> propose(@RequestParam Long supportRequestId,
                                                          @RequestParam LocalDate date,
                                                          @RequestParam Integer duration) {
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