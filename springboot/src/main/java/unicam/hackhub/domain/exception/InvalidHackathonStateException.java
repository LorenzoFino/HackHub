package unicam.hackhub.domain.exception;

public class InvalidHackathonStateException extends RuntimeException {
    public InvalidHackathonStateException(String message) {
        super(message);
    }
}
