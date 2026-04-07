package unicam.hackhub.domain.observer;

import unicam.hackhub.domain.model.HackathonStatus;

/**
 * Observer Pattern — concrete observer for the judge.
 * Notifies the judge when the hackathon enters EVALUATION state
 * so they know it is time to evaluate submissions.
 */
public class JudgeNotificationObserver implements HackathonObserver {

    private final String judgeEmail;

    public JudgeNotificationObserver(String judgeEmail) {
        this.judgeEmail = judgeEmail;
    }

    @Override
    public void onStateChanged(Long hackathonId, HackathonStatus.StateType newState) {
        if (newState == HackathonStatus.StateType.EVALUATION) {
            System.out.println("[NOTIFICATION] Judge " + judgeEmail +
                    " — hackathon " + hackathonId +
                    " is now in EVALUATION. Please evaluate all submissions.");
        }
    }
}