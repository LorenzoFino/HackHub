package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.dto.command.CreateValuationCommand;
import unicam.hackhub.application.dto.command.UpdateValuationCommand;
import unicam.hackhub.application.dto.response.ValuationResult;
import unicam.hackhub.application.valuation.ValuationService;
import unicam.hackhub.presentation.dto.request.CreateValuationRequest;
import unicam.hackhub.presentation.dto.request.UpdateValuationRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/valuations")
public class StaffController {

    private final ValuationService valuationService;

    public StaffController(ValuationService valuationService) {
        this.valuationService = valuationService;
    }

    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<ValuationResult>> getAllByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(valuationService.findAllByHackathon(hackathonId));
    }

    @PostMapping
    public ResponseEntity<ValuationResult> release(@Valid @RequestBody CreateValuationRequest request) {
        CreateValuationCommand command = new CreateValuationCommand(
                request.vote(), request.judgement(),
                request.submissionId(), request.judgeId()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(valuationService.releaseValuation(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ValuationResult> update(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateValuationRequest request) {
        UpdateValuationCommand command = new UpdateValuationCommand(
                request.vote(), request.judgement()
        );
        return ResponseEntity.ok(valuationService.updateValuation(id, command));
    }
}