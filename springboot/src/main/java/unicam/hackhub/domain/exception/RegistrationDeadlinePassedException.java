package unicam.hackhub.domain.exception;

public class RegistrationDeadlinePassedException extends RuntimeException {
    public RegistrationDeadlinePassedException() {
        super("Registration deadline has passed");
    }
}
