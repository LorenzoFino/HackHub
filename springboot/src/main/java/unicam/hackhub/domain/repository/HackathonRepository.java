package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Hackathon;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Hackathon.
 * Implemented by JpaHackathonRepository in the infrastructure layer.
 */
public interface HackathonRepository {

    Hackathon save(Hackathon hackathon);

    Optional<Hackathon> findById(Long id);

    List<Hackathon> findAll();

    void delete(Hackathon hackathon);

    /**Returns true if the team (by name) is registered in any hackathon */
    boolean existsByRegisteredTeams_Name(String teamName);
}