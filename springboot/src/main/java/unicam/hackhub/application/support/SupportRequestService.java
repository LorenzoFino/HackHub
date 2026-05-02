package unicam.hackhub.application.support;

import unicam.hackhub.application.dto.command.CreateSupportRequestCommand;
import unicam.hackhub.presentation.dto.response.SupportRequestResponse;
import java.util.List;

public interface SupportRequestService {

    /**
     * Sends a new support request from a team to the mentor pool.
     *
     * @param command contains description, teamName, hackathonId
     * @return the created SupportRequestResponse DTO
     */
    SupportRequestResponse sendSupportRequest(CreateSupportRequestCommand command);

    /**
     * Cancels an existing support request.
     * Only allowed if the request is still PENDING.
     *
     * @param supportRequestId id of the request to cancel
     */
    void cancelSupportRequest(Long supportRequestId);

    /**
     * Returns all support requests for a given hackathon — used by the mentor.
     *
     * @param hackathonId id of the hackathon
     * @return list of SupportRequestResponse DTOs
     */
    List<SupportRequestResponse> findAllByHackathon(Long hackathonId);

    /**
     * Returns all support requests sent by a given team.
     *
     * @param teamName name of the team
     * @return list of SupportRequestResponse DTOs
     */
    List<SupportRequestResponse> findAllByTeam(String teamName);

    /**
     * Returns a support request by id.
     *
     * @param id id of the request
     * @return the SupportRequestResponse DTO
     */
    SupportRequestResponse findById(Long id);
}