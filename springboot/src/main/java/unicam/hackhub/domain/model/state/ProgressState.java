package unicam.hackhub.domain.model.state;

import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Submission;
import unicam.hackhub.domain.model.Team;

import java.time.LocalDate;

/**
 * State: PROGRESS
 * Registered teams can add and update their submissions.
 * Next transition: EVALUATION (when the hackathon period ends)
 */
public class ProgressState extends AbstractHackathonState {

    public ProgressState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public void addSubmission(Team team, Submission submission) {
        if (!hackathon.hasTeam(team))
            throw new IllegalStateException("Team is not registered to this hackathon");
    }

    @Override
    public void updateSubmission(Team team, Submission submission) {
        if (!hackathon.hasTeam(team))
            throw new IllegalStateException("Team is not registered to this hackathon");
    }

    @Override
    public void toNextState() {
        if (hackathon.getPeriod().isAfterPeriod(LocalDate.now()))
            hackathon.changeState(hackathon.getStatus().getNextState());
    }
}