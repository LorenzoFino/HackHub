package unicam.hackhub.application.hackathon;

import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.RoleAssignment;

import java.util.List;

/**
 * Application service for hackathon management.
 * Covers the Organizer use cases from the sequence diagrams.
 */
public interface HackathonService {

    /** Creates a new hackathon — only an Organizer can do this */
    Hackathon createHackathon(Hackathon hackathon);

    /** Updates an existing hackathon — only allowed during SUBSCRIPTION */
    Hackathon updateHackathon(Long hackathonId, Hackathon updatedHackathon);

    /** Deletes an existing hackathon — only allowed during SUBSCRIPTION */
    void deleteHackathon(Long hackathonId);

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

    /**
     * Assigns a role to a staff member for a specific hackathon.
     * A staff member can hold different roles in different hackathons.
     *
     * @param hackathonId   id of the hackathon
     * @param membroStaffId id of the staff member
     * @param role         role name (e.g. "ORGANIZER", "JUDGE", "MENTOR")
     * @return the created RoleAssignment
     */
    RoleAssignment roleAssign(Long hackathonId, Long membroStaffId, String role);
}