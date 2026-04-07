package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.MentorCall;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MentorCall.
 * Implemented by JpaCallRepository in the infrastructure layer.
 */
public interface CallRepository {

    MentorCall save(MentorCall call);

    Optional<MentorCall> findById(Long id);

    List<MentorCall> findAllBySupportRequestId(Long supportRequestId);
}