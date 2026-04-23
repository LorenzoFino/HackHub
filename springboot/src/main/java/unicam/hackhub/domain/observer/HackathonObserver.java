package unicam.hackhub.domain.observer;

import unicam.hackhub.domain.model.HackathonStatus;

/**
 * Observer Pattern — observer interface.
 * Implemented by any class that needs to react
 * to hackathon state changes.
 */
public interface HackathonObserver {

    /**
     * Called when the hackathon changes state.
     *
     * @param hackathonId id of the hackathon that changed state
     * @param newState    the new state
     */
    void onStateChanged(Long hackathonId, HackathonStatus.StateType newState);
}