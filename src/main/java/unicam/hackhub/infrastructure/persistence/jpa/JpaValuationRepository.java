package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.Valuation;
import unicam.hackhub.domain.repository.ValuationRepository;

import java.util.List;

/**
 * JPA implementation of ValuationRepository.
 */
@Repository
public interface JpaValuationRepository extends JpaRepository<Valuation, Long>, ValuationRepository {

    List<Valuation> findAllBySubmissionHackathonId(Long hackathonId);
}