package unicam.hackhub.application.valuation;

import unicam.hackhub.application.dto.command.CreateValuationCommand;
import unicam.hackhub.application.dto.command.UpdateValuationCommand;
import unicam.hackhub.application.dto.response.ValuationResult;
import java.util.List;

/**
 * Application service for valuation management.
 * Covers the Judge use cases from the sequence diagrams.
 */
public interface ValuationService {

    /**
     * Releases a new valuation for a submission.
     * Only allowed during the EVALUATION state.
     *
     * @param command contains vote, judgement, submissionId, judgeId
     * @return the created ValuationResult DTO
     */
    ValuationResult releaseValuation(CreateValuationCommand command);

    /**
     * Updates an existing valuation.
     * Only allowed during the EVALUATION state.
     *
     * @param valuationId id of the valuation to update
     * @param command     contains the updated vote and judgement
     * @return the updated ValuationResult DTO
     */
    ValuationResult updateValuation(Long valuationId, UpdateValuationCommand command);

    /**
     * Returns all valuations for a given hackathon.
     *
     * @param hackathonId id of the hackathon
     * @return list of ValuationResult DTOs
     */
    List<ValuationResult> findAllByHackathon(Long hackathonId);
}