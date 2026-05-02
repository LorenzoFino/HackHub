package unicam.hackhub.application.valuation;

import unicam.hackhub.application.dto.command.CreateValuationCommand;
import unicam.hackhub.application.dto.command.UpdateValuationCommand;
import unicam.hackhub.application.dto.response.ValuationResult;
import java.util.List;

public interface ValuationService {
    ValuationResult releaseValuation(CreateValuationCommand command);
    ValuationResult updateValuation(Long valuationId, UpdateValuationCommand command);
    List<ValuationResult> findAllByHackathon(Long hackathonId);
}