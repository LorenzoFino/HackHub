package unicam.hackhub.application.submission;

import unicam.hackhub.application.dto.command.CreateSubmissionCommand;
import unicam.hackhub.application.dto.response.SubmissionResult;
import java.util.List;

/**
 * Application service for submission management.
 * Covers the Team Member use cases from the sequence diagrams.
 */
public interface SubmissionService {

    /**
     * Sends a new submission for a hackathon.
     * Only allowed during the PROGRESS state.
     *
     * @param command contains title, description, link, teamName, hackathonId
     * @return the created SubmissionResult DTO
     */
    SubmissionResult sendSubmission(CreateSubmissionCommand command);

    /**
     * Updates an existing submission.
     * Only allowed during the PROGRESS state.
     *
     * @param submissionId id of the submission to update
     * @param command      contains the updated fields
     * @return the updated SubmissionResult DTO
     */
    SubmissionResult updateSubmission(Long submissionId, CreateSubmissionCommand command);

    /**
     * Returns all submissions for a given hackathon.
     *
     * @param hackathonId id of the hackathon
     * @return list of SubmissionResult DTOs
     */
    List<SubmissionResult> findAllByHackathon(Long hackathonId);

    /**
     * Returns the submission of a specific team for a given hackathon.
     *
     * @param teamName    name of the team
     * @param hackathonId id of the hackathon
     * @return the SubmissionResult DTO
     */
    SubmissionResult findByTeamAndHackathon(String teamName, Long hackathonId);
}