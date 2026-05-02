package unicam.hackhub.domain.exception;

public class TeamAlreadyRegisteredException extends RuntimeException {
    public TeamAlreadyRegisteredException(String teamName) {
        super("Team already registered: " + teamName);
    }
}
