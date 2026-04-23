package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Staff;

import java.util.List;

/**
 * Application service for hackathon management.
 * Covers the Organizer use cases from the sequence diagrams.
 */
public interface HackathonService {

    /** Creates a new hackathon — only an Organizer can do this */
    Hackathon createHackathon(Hackathon hackathon);

    /** Adds a mentor to an existing hackathon */
    void addMentor(Long hackathonId, Long mentorId);

    /** Declares the winning team — only after all submissions are evaluated */
    void declareWinner(Long hackathonId, String teamName);

    /** Returns all hackathons in the system */
    List<Hackathon> findAll();

    /** Returns a hackathon by id */
    Hackathon findById(Long id);

    /** Advances the hackathon to the next state */
    void toNextState(Long hackathonId);
}