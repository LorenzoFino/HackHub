package unicam.hackhub.application.submission;

import unicam.hackhub.domain.model.Submission;

import java.util.List;

/**
 * Application service for submission management.
 * Covers the Team Member use cases from the sequence diagrams.
 */
public interface SubmissionService {

    /** Sends a new submission for a team — only allowed in PROGRESS state */
    Submission sendSubmission(Submission submission);

    /** Updates an existing submission — only allowed before the deadline */
    Submission updateSubmission(Long submissionId, Submission updatedSubmission);

    /** Returns all submissions for a given hackathon */
    List<Submission> findAllByHackathon(Long hackathonId);

    /** Returns the submission of a specific team for a given hackathon */
    Submission findByTeamAndHackathon(String teamName, Long hackathonId);
}