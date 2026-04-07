package unicam.hackhub.domain.observer;

import unicam.hackhub.domain.model.HackathonStatus;

/**
 * Observer Pattern — concrete observer for teams.
 * Notifies a team when the hackathon ends
 * so they know the winner has been declared.
 */
public class TeamNotificationObserver implements HackathonObserver {

    private final String teamName;

    public TeamNotificationObserver(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public void onStateChanged(Long hackathonId, HackathonStatus.StateType newState) {
        if (newState == HackathonStatus.StateType.ENDED) {
            System.out.println("[NOTIFICATION] Team " + teamName +
                    " — hackathon " + hackathonId +
                    " has ended. The winner has been declared.");
        }
    }
}