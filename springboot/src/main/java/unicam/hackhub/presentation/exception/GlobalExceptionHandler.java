package unicam.hackhub.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import unicam.hackhub.domain.exception.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 Not Found
    @ExceptionHandler({
            HackathonNotFoundException.class,
            TeamNotFoundException.class,
            UserNotFoundException.class,
            StaffNotFoundException.class,
            SubmissionNotFoundException.class,
            ValuationNotFoundException.class,
            ReportNotFoundException.class,
            SupportRequestNotFoundException.class,
            InvitationNotFoundException.class,
            CallNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409 Conflict
    @ExceptionHandler({
            InvalidHackathonStateException.class,
            TeamAlreadyRegisteredException.class,
            UserAlreadyInTeamException.class,
            MentorAlreadyAssignedException.class,
            SubmissionAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 400 Bad Request
    @ExceptionHandler({
            InvalidVoteException.class,
            RegistrationDeadlinePassedException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // 500 Internal Server Error — fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}