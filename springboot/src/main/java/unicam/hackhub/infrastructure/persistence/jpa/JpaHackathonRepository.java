package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.repository.HackathonRepository;

/**
 * JPA implementation of HackathonRepository.
 * Spring Data JPA provides all standard CRUD operations automatically.
 */
@Repository
public interface JpaHackathonRepository extends JpaRepository<Hackathon, Long>, HackathonRepository {
}