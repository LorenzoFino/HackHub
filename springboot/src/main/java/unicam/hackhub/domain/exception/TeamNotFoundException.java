package unicam.hackhub.domain.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(String name) {
        super("Team not found: " + name);
    }
}