package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.SupportRequest;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SupportRequest.
 * Implemented by JpaSupportRequestRepository in the infrastructure layer.
 */
public interface SupportRequestRepository {

    SupportRequest save(SupportRequest supportRequest);

    Optional<SupportRequest> findById(Long id);

    List<SupportRequest> findAllByHackathonId(Long hackathonId);

    List<SupportRequest> findAllByTeamName(String teamName);

    void deleteAllByHackathonId(Long hackathonId);
}