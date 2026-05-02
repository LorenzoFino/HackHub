package unicam.hackhub.domain.exception;

public class MentorAlreadyAssignedException extends RuntimeException {
    public MentorAlreadyAssignedException(Long mentorId) {
        super("Mentor already assigned: " + mentorId);
    }
}
