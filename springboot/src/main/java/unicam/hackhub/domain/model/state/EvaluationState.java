package unicam.hackhub.domain.model.state;

import unicam.hackhub.domain.model.Hackathon;

/**
 * State: EVALUATION
 * The judge can evaluate team submissions.
 * The organizer can declare the winner once all submissions have been evaluated.
 * Next transition: ENDED (after the winner is declared)
 */
public class EvaluationState extends AbstractHackathonState {

    public EvaluationState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public void valuateSubmission(String teamName, Integer vote, String judgement) {
        if (vote < 0 || vote > 10)
            throw new IllegalArgumentException("Vote must be between 0 and 10");
    }

    @Override
    public void updateValuation(String teamName, Integer vote, String judgement) {
        if (vote < 0 || vote > 10)
            throw new IllegalArgumentException("Vote must be between 0 and 10");
    }

    @Override
    public void declareWinner(String teamName) {
        boolean teamExists = hackathon.getRegisteredTeams().stream()
                .anyMatch(t -> t.getName().equals(teamName));

        if (!teamExists)
            throw new IllegalArgumentException("Team not found: " + teamName);
    }

    @Override
    public void toNextState() {
        // Evaluation state can only end when the organizer declares a winner
        // via declareWinner() — the scheduler cannot advance this state automatically
    }

}