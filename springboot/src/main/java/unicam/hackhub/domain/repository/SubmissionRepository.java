package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.domain.model.SupportRequest;
import unicam.hackhub.domain.model.Team;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Submission.
 * Implemented by JpaSubmissionRepository in the infrastructure layer.
 */
public interface SubmissionRepository {

    Submission save(Submission submission);

    Optional<Submission> findById(Long id);

    Optional<Submission> findByTeamAndHackathonId(Team team, Long hackathonId);

    List<Submission> findAllByHackathonId(Long hackathonId);
}