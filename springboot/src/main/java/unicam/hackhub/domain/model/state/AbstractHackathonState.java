package unicam.hackhub.domain.model.state;

import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.domain.model.Team;

/**
 * Base class for all hackathon states (State Pattern).
 * By default every operation throws IllegalStateException.
 * Each concrete state overrides only the operations it allows.
 */
public abstract class AbstractHackathonState implements HackathonState {

    protected final Hackathon hackathon;

    protected AbstractHackathonState(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    @Override
    public void registerTeam(Team team) {
        throw new IllegalStateException("Cannot register a team in this state");
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Cannot add a submission in this state");
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        throw new IllegalStateException("Cannot update a submission in this state");
    }

    @Override
    public void valuateSubmission(String teamName, Integer vote, String judgement) {
        throw new IllegalStateException("Cannot valuate a submission in this state");
    }

    @Override
    public void updateValuation(String teamName, Integer vote, String judgement) {
        throw new IllegalStateException("Cannot update a valuation in this state");
    }

    @Override
    public void declareWinner(String teamName) {
        throw new IllegalStateException("Cannot declare a winner in this state");
    }

    @Override
    public void toNextState() {
        // Default: no transition (e.g. ENDED)
    }
}