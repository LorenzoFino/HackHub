package unicam.hackhub.domain.exception;

public class SubmissionAlreadyExistsException extends RuntimeException {
    public SubmissionAlreadyExistsException(String teamName) {
        super("Submission already exists for team: " + teamName);
    }
}
