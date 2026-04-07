package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Valuation;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Valuation.
 * Implemented by JpaValuationRepository in the infrastructure layer.
 */
public interface ValuationRepository {

    Valuation save(Valuation valuation);

    Optional<Valuation> findById(Long id);

    List<Valuation> findAllBySubmissionHackathonId(Long hackathonId);
}