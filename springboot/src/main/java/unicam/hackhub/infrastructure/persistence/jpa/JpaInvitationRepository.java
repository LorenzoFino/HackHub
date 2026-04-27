package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.Invitation;
import unicam.hackhub.domain.repository.InvitationRepository;

import java.util.List;

/**
 * JPA implementation of InvitationRepository.
 */
@Repository
public interface JpaInvitationRepository extends JpaRepository<Invitation, Long>, InvitationRepository {
    List<Invitation> findAllByTeam_Name(String teamName);
}