package unicam.hackhub.domain.model.state;

import unicam.hackhub.domain.model.Hackathon;

/**
 * State: ENDED
 * The hackathon is finished and the winner has been declared.
 * No operations are allowed anymore.
 * All methods inherit the default behaviour from AbstractHackathonState
 * which throws IllegalStateException.
 */
public class EndedState extends AbstractHackathonState {

    public EndedState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public void toNextState() {
        // Final state — no further transitions possible
    }
}