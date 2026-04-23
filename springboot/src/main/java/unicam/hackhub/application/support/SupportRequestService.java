package unicam.hackhub.application.support;

import unicam.hackhub.domain.model.SupportRequest;

import java.util.List;

/**
 * Application service for support request management.
 * Covers the Team Member and Mentor use cases from the sequence diagrams.
 */
public interface SupportRequestService {

    /** Sends a new support request from a team to a mentor */
    SupportRequest sendSupportRequest(SupportRequest supportRequest);

    /** Cancels an existing support request */
    void cancelSupportRequest(Long supportRequestId);

    /** Returns all support requests for a given hackathon — used by the mentor */
    List<SupportRequest> findAllByHackathon(Long hackathonId);

    /** Returns all support requests sent by a given team */
    List<SupportRequest> findAllByTeam(String teamName);

    /** Returns a support request by id */
    SupportRequest findById(Long id);
}