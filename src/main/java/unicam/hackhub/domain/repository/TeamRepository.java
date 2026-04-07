package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Team;

import java.util.Optional;

/**
 * Repository interface for Team.
 * Implemented by JpaTeamRepository in the infrastructure layer.
 */
public interface TeamRepository {

    Team save(Team team);

    Optional<Team> findById(String name);

    boolean existsById(String name);
}