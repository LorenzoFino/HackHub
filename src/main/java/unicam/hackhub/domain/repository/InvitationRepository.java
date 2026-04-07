package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Invitation;

import java.util.Optional;

/**
 * Repository interface for Invitation.
 * Implemented by JpaInvitationRepository in the infrastructure layer.
 */
public interface InvitationRepository {

    Invitation save(Invitation invitation);

    Optional<Invitation> findById(Long id);
}