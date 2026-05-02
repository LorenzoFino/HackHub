package unicam.hackhub.domain.exception;

public class StaffNotFoundException extends RuntimeException {
    public StaffNotFoundException(Long id) {
        super("Staff not found: " + id);
    }
    public StaffNotFoundException(String email) {
        super("Staff not found: " + email);
    }
}