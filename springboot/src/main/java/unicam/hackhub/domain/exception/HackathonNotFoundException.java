package unicam.hackhub.domain.exception;

public class HackathonNotFoundException extends RuntimeException {
    public HackathonNotFoundException(Long id) {
        super("Hackathon not found: " + id);
    }
}