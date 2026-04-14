package unicam.hackhub.application.call;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.MentorCall;
import unicam.hackhub.domain.model.SupportRequest;
import unicam.hackhub.domain.repository.CallRepository;
import unicam.hackhub.domain.repository.SupportRequestRepository;
import unicam.hackhub.infrastructure.services.calendar.MockCalendarAdapter;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of CallService.
 * Orchestrates domain objects, repositories and the external Calendar adapter.
 */
@Service
public class CallServiceImpl implements CallService {

    private final CallRepository callRepository;
    private final SupportRequestRepository supportRequestRepository;
    private final MockCalendarAdapter calendarAdapter;

    public CallServiceImpl(CallRepository callRepository,
                           SupportRequestRepository supportRequestRepository,
                           MockCalendarAdapter calendarAdapter) {
        this.callRepository = callRepository;
        this.supportRequestRepository = supportRequestRepository;
        this.calendarAdapter = calendarAdapter;
    }

    @Override
    public MentorCall proposeCall(Long supportRequestId, LocalDate date, int duration) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Support request not found: " + supportRequestId));

        if (supportRequest.getStatus() != SupportRequest.RequestStatus.PENDING)
            throw new IllegalStateException("Support request is not pending");

        String mentorEmail = supportRequest.getHackathon()
                .getMentors().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No mentor assigned"))
                .getEmail();

        String teamName = supportRequest.getTeam().getName();

        // Book slot via external Calendar system
        String link = calendarAdapter.bookSlot(mentorEmail, teamName, date, duration);

        // Create the call
        MentorCall call = new MentorCall(link, date, duration, supportRequest);

        // Update support request status
        supportRequest.setStatus(SupportRequest.RequestStatus.ACCEPTED);
        supportRequestRepository.save(supportRequest);

        return callRepository.save(call);
    }

    @Override
    public void cancelCall(Long callId) {
        MentorCall call = findById(callId);

        // Cancel slot via external Calendar system
        calendarAdapter.cancelSlot(call.getLink());

        call.setStatus(MentorCall.CallStatus.CANCELLED);
        callRepository.save(call);
    }

    @Override
    public MentorCall findById(Long callId) {
        return callRepository.findById(callId)
                .orElseThrow(() -> new IllegalArgumentException("Call not found: " + callId));
    }

    @Override
    public List<MentorCall> findAllBySupportRequest(Long supportRequestId) {
        return callRepository.findAllBySupportRequestId(supportRequestId);
    }
}