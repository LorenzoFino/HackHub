package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.MentorCall;
import unicam.hackhub.domain.repository.CallRepository;

import java.util.List;

/**
 * JPA implementation of CallRepository.
 */
@Repository
public interface JpaCallRepository extends JpaRepository<MentorCall, Long>, CallRepository {

    List<MentorCall> findAllBySupportRequestId(Long supportRequestId);
}