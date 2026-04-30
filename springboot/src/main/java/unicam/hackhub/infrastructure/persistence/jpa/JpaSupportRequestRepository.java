package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import unicam.hackhub.domain.model.SupportRequest;
import unicam.hackhub.domain.repository.SupportRequestRepository;

import java.util.List;

/**
 * JPA implementation of SupportRequestRepository.
 */
@Repository
public interface JpaSupportRequestRepository extends JpaRepository<SupportRequest, Long>,
        SupportRequestRepository {

    List<SupportRequest> findAllByHackathonId(Long hackathonId);

    List<SupportRequest> findAllByTeamName(String teamName);

    @Transactional
    void deleteAllByHackathonId(Long hackathonId);
}