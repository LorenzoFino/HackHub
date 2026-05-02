package unicam.hackhub.domain.exception;

public class UserAlreadyInTeamException extends RuntimeException {
    public UserAlreadyInTeamException(String email) {
        super("User already in a team: " + email);
    }
}