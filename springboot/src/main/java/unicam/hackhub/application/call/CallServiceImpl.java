package unicam.hackhub.application.call;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.exception.*;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;
import unicam.hackhub.infrastructure.services.calendar.MockCalendarAdapter;
import unicam.hackhub.presentation.dto.mapper.SupportRequestMapper;
import unicam.hackhub.presentation.dto.response.SupportRequestResponse;

import java.time.LocalDate;

/**
 * Implementation of CallService.
 * Orchestrates domain objects, repositories, exception handling
 * and the external Calendar adapter.
 */
@Service
public class CallServiceImpl implements CallService {

    private final CallRepository callRepository;
    private final SupportRequestRepository supportRequestRepository;
    private final MockCalendarAdapter calendarAdapter;
    private final SupportRequestMapper supportRequestMapper;

    public CallServiceImpl(CallRepository callRepository,
                           SupportRequestRepository supportRequestRepository,
                           MockCalendarAdapter calendarAdapter,
                           SupportRequestMapper supportRequestMapper) {
        this.callRepository = callRepository;
        this.supportRequestRepository = supportRequestRepository;
        this.calendarAdapter = calendarAdapter;
        this.supportRequestMapper = supportRequestMapper;
    }

    @Override
    public SupportRequestResponse proposeCall(Long supportRequestId, LocalDate date, Integer duration) {
        SupportRequest supportRequest = supportRequestRepository.findById(supportRequestId)
                .orElseThrow(() -> new SupportRequestNotFoundException(supportRequestId));

        if (supportRequest.getStatus() != SupportRequest.RequestStatus.PENDING)
            throw new IllegalStateException("Support request is not pending");

        String mentorEmail = supportRequest.getHackathon()
                .getMentors().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No mentor assigned"))
                .getEmail();

        // Book slot via external Calendar system
        String link = calendarAdapter.bookSlot(mentorEmail,
                supportRequest.getTeam().getName(), date, duration);

        // Create the call and update support request status
        MentorCall call = new MentorCall(link, date, duration, supportRequest);
        supportRequest.setStatus(SupportRequest.RequestStatus.ACCEPTED);
        supportRequestRepository.save(supportRequest);
        callRepository.save(call);

        return supportRequestMapper.toResponse(supportRequest);
    }

    @Override
    public void cancelCall(Long callId) {
        MentorCall call = callRepository.findById(callId)
                .orElseThrow(() -> new CallNotFoundException(callId));

        // Release the Calendar slot
        calendarAdapter.cancelSlot(call.getLink());
        call.setStatus(MentorCall.CallStatus.CANCELLED);
        callRepository.save(call);
    }
}