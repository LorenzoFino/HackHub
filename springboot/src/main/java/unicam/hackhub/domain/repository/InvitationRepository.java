package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Invitation;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Invitation.
 * Implemented by JpaInvitationRepository in the infrastructure layer.
 */
public interface InvitationRepository {

    Invitation save(Invitation invitation);

    void deleteAll(List<Invitation> invitations);

    Optional<Invitation> findById(Long id);

    List<Invitation> findAllByTeam_Name(String teamName);

    List<Invitation> findAllByTeam_NameAndStatus(String teamName, Invitation.InvitationStatus status);
}