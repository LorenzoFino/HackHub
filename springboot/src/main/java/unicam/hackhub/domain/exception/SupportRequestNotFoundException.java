package unicam.hackhub.domain.exception;

public class SupportRequestNotFoundException extends RuntimeException {
    public SupportRequestNotFoundException(Long id) {
        super("Support request not found: " + id);
    }
}