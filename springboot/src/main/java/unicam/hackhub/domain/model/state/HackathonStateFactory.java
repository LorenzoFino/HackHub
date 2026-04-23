package unicam.hackhub.domain.model.state;

import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.HackathonStatus;

/**
 * Factory for creating state objects (State Pattern).
 * Centralizes instantiation logic so that Hackathon
 * does not need to know the concrete state classes.
 */
public class HackathonStateFactory {

    private HackathonStateFactory() {}

    public static HackathonState createState(HackathonStatus.StateType type, Hackathon hackathon) {
        return switch (type) {
            case SUBSCRIPTION -> new SubscriptionState(hackathon);
            case PROGRESS     -> new ProgressState(hackathon);
            case EVALUATION   -> new EvaluationState(hackathon);
            case ENDED        -> new EndedState(hackathon);
        };
    }
}