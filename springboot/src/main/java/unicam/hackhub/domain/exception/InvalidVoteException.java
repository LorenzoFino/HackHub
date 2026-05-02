package unicam.hackhub.domain.exception;

public class InvalidVoteException extends RuntimeException {
    public InvalidVoteException(int vote) {
        super("Invalid vote: " + vote + ". Must be between 0 and 10");
    }
}
