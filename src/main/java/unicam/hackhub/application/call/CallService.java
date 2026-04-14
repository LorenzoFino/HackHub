package unicam.hackhub.application.call;

import unicam.hackhub.domain.model.MentorCall;

import java.time.LocalDate;
import java.util.List;

/**
 * Application service for mentor call management.
 * Covers the Mentor use cases from the sequence diagrams.
 * Uses the external Calendar system via MockCalendarAdapter.
 */
public interface CallService {

    /** Proposes a call to a team in response to a support request */
    MentorCall proposeCall(Long supportRequestId, LocalDate date, int duration);

    /** Cancels an existing call */
    void cancelCall(Long callId);

    /** Returns a call by id */
    MentorCall findById(Long callId);

    /** Returns all calls for a given support request */
    List<MentorCall> findAllBySupportRequest(Long supportRequestId);
}