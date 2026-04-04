package unicam.hackhub.domain.model.state;

import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.domain.model.Team;

/**
 * State Pattern — interface.
 * Defines the behavior of the hackathon in each phase of its lifecycle.
 * Each concrete implementation only allows the operations permitted in that state.
 */
public interface HackathonState {

    /** Registers a team to the hackathon */
    void registerTeam(Team team);

    /** Adds a submission for a team (only in PROGRESS) */
    void addSubmission(Team team, Submission sottomissione);

    /** Updates the submission of a team (only in PROGRESS) */
    void updateSubmission(Team team, Submission sottomissione);

    /** Judge evaluates a team's submission (only in EVALUATION) */
    void valuateSubmission(String nomeTeam, int voto, String giudizio);

    /** Judge updates an existing valuation (only in EVALUATION) */
    void updateValuation(String nomeTeam, int voto, String giudizio);

    /** Organizer declares the winning team (only in EVALUATION) */
    void declareWinner(String nomeTeam);

    /** Advances to the next state if conditions are met */
    void toNextState();
}