package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.domain.model.Team;
import unicam.hackhub.domain.repository.SubmissionRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of SubmissionRepository.
 */
@Repository
public interface JpaSubmissionRepository extends JpaRepository<Submission, Long>, SubmissionRepository {

    Optional<Submission> findByTeamAndHackathon(Team team, Hackathon hackathon);

    List<Submission> findAllByHackathonId(Long hackathonId);
}