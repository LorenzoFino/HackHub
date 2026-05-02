package unicam.hackhub.domain.exception;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(Long id) {
        super("Invitation not found: " + id);
    }
}