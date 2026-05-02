package unicam.hackhub.domain.exception;

public class CallNotFoundException extends RuntimeException {
    public CallNotFoundException(Long id) {
        super("Call not found: " + id);
    }
}