package unicam.hackhub.application.call;

import unicam.hackhub.presentation.dto.response.SupportRequestResponse;
import java.time.LocalDate;
import java.util.List;

public interface CallService {

    /**
     * Proposes a call to a team in response to a support request.
     * Books a slot via the external Calendar system.
     * Updates the support request status to ACCEPTED.
     *
     * @param supportRequestId id of the support request
     * @param date             date of the call
     * @param duration         duration in minutes
     * @return the updated SupportRequestResponse DTO
     */
    SupportRequestResponse proposeCall(Long supportRequestId, LocalDate date, Integer duration);

    /**
     * Cancels an existing call and releases the Calendar slot.
     *
     * @param callId id of the call to cancel
     */
    void cancelCall(Long callId);
}