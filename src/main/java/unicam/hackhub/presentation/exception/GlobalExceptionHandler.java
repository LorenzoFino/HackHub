package unicam.hackhub.presentation.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for the REST API.
 * Catches exceptions thrown by the application and returns
 * a structured JSON error response.
 * Excludes H2 console requests.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Handles invalid arguments — e.g. team not found, hackathon not found */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex,
                                                                     HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/h2-console"))
            return null;
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /** Handles invalid state transitions — e.g. registering a team when deadline has passed */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex,
                                                                  HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/h2-console"))
            return null;
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /** Handles any other unexpected exception */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex,
                                                             HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/h2-console"))
            return null;
        ex.printStackTrace();
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