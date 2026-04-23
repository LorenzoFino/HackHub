package unicam.hackhub.domain.observer;

/**
 * Observer Pattern — observable interface.
 * Implemented by Hackathon to allow observers to register
 * and receive state change notifications.
 */
public interface HackathonObservable {

    /** Registers an observer */
    void addObserver(HackathonObserver observer);

    /** Removes an observer */
    void removeObserver(HackathonObserver observer);

    /** Notifies all registered observers of a state change */
    void notifyObservers();
}