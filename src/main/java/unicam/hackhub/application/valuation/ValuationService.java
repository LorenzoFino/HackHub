package unicam.hackhub.application.valuation;

import unicam.hackhub.domain.model.Valuation;

import java.util.List;

/**
 * Application service for valuation management.
 * Covers the Judge use cases from the sequence diagrams.
 */
public interface ValuationService {

    /** Releases a new valuation for a submission — only allowed in EVALUATION state */
    Valuation releaseValuation(Valuation valuation);

    /** Updates an existing valuation */
    Valuation updateValuation(Long valuationId, Valuation updatedValuation);

    /** Returns all valuations for a given hackathon */
    List<Valuation> findAllByHackathon(Long hackathonId);
}