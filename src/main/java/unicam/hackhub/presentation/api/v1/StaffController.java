package unicam.hackhub.presentation.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.valuation.ValuationService;
import unicam.hackhub.domain.model.Valuation;

import java.util.List;

/**
 * REST controller for judge operations.
 * Covers the Judge use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/valuations")
public class StaffController {

    private final ValuationService valuationService;

    public StaffController(ValuationService valuationService) {
        this.valuationService = valuationService;
    }

    /** GET /api/v1/valuations/hackathon/{hackathonId} — returns all valuations for a hackathon */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<Valuation>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(valuationService.findAllByHackathon(hackathonId));
    }

    /** POST /api/v1/valuations — releases a new valuation */
    @PostMapping
    public ResponseEntity<Valuation> release(@RequestBody Valuation valuation) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valuationService.releaseValuation(valuation));
    }

    /** PUT /api/v1/valuations/{id} — updates an existing valuation */
    @PutMapping("/{id}")
    public ResponseEntity<Valuation> update(@PathVariable Long id,
                                            @RequestBody Valuation valuation) {
        return ResponseEntity.ok(valuationService.updateValuation(id, valuation));
    }
}