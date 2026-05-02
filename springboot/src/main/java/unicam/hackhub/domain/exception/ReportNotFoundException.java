package unicam.hackhub.domain.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(Long id) {
        super("Report not found: " + id);
    }
}