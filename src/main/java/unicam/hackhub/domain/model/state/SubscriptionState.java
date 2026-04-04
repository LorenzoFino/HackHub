package unicam.hackhub.domain.model.state;

import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Team;

import java.time.LocalDate;

/**
 * State: SUBSCRIPTION
 * Teams can register to the hackathon until the deadline.
 * Next transition: PROGRESS (when the hackathon period begins)
 */
public class SubscriptionState extends AbstractHackathonState {

    public SubscriptionState(Hackathon hackathon) {
        super(hackathon);
    }

    @Override
    public void registerTeam(Team team) {
        if (LocalDate.now().isAfter(hackathon.getRegistrationDeadline()))
            throw new IllegalStateException("Registration deadline has passed");

        if (team.getMembers().size() > hackathon.getMaxTeamSize())
            throw new IllegalArgumentException("Team exceeds the maximum allowed size");

        if (hackathon.hasTeam(team))
            throw new IllegalStateException("Team is already registered to this hackathon");
    }

    @Override
    public void toNextState() {
        if (hackathon.getPeriod().isWithinPeriod(LocalDate.now()))
            hackathon.changeState(hackathon.getStatus().getNextState());
    }
}