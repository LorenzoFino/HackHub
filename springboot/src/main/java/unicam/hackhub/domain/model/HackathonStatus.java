package unicam.hackhub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

/**
 * Embedded value object that tracks the current state of the hackathon in the DB.
 * Used by the State Pattern to know which phase the hackathon is in.
 */
@Setter
@Getter
@Embeddable
public class HackathonStatus {

    @Enumerated(EnumType.STRING)
    @Column(name = "current_state", nullable = false)
    private StateType currentState;

    public HackathonStatus() {
        this.currentState = StateType.SUBSCRIPTION;
    }

    public HackathonStatus(StateType currentState) {
        this.currentState = currentState;
    }

    public enum StateType {
        SUBSCRIPTION,  // registrations open
        PROGRESS,      // hackathon in progress, submissions accepted
        EVALUATION,    // judge evaluates submissions
        ENDED          // finished, winner proclaimed
    }

    /** Returns the next state type in the lifecycle sequence */
    public StateType getNextState() {
        return switch (currentState) {
            case SUBSCRIPTION -> StateType.PROGRESS;
            case PROGRESS     -> StateType.EVALUATION;
            default           -> StateType.ENDED;
        };
    }
}