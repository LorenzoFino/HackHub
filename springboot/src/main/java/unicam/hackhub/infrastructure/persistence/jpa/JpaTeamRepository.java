package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.Team;
import unicam.hackhub.domain.repository.TeamRepository;

/**
 * JPA implementation of TeamRepository.
 */
@Repository
public interface JpaTeamRepository extends JpaRepository<Team, String>, TeamRepository {
}