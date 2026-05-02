package unicam.hackhub.domain.exception;

public class ValuationNotFoundException extends RuntimeException {
    public ValuationNotFoundException(Long id) {
        super("Valuation not found: " + id);
    }
}