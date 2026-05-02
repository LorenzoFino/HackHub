package unicam.hackhub.application.hackathon;

import unicam.hackhub.application.dto.command.*;
import unicam.hackhub.application.dto.response.HackathonResult;
import unicam.hackhub.domain.model.RoleAssignment;
import java.util.List;

/**
 * Application service for hackathon management.
 * Covers the Organizer use cases from the sequence diagrams.
 */
public interface HackathonService {

    /**
     * Creates a new hackathon — only an Organizer can do this.
     *
     * @param command contains all hackathon fields including staff ids
     * @return the created HackathonResult DTO
     */
    HackathonResult createHackathon(CreateHackathonCommand command);

    /**
     * Updates an existing hackathon — only allowed during the SUBSCRIPTION state.
     * Notifies registered teams via email if any.
     *
     * @param hackathonId id of the hackathon to update
     * @param command     contains the updated fields
     * @return the updated HackathonResult DTO
     */
    HackathonResult updateHackathon(Long hackathonId, UpdateHackathonCommand command);

    /**
     * Deletes an existing hackathon — only allowed during the SUBSCRIPTION state.
     * Notifies registered teams via email and unregisters them automatically.
     *
     * @param hackathonId id of the hackathon to delete
     */
    void deleteHackathon(Long hackathonId);

    /**
     * Adds a mentor to an existing hackathon.
     *
     * @param hackathonId id of the hackathon
     * @param mentorId    id of the mentor to add
     */
    void addMentor(Long hackathonId, Long mentorId);

    /**
     * Declares the winning team and processes the prize payment.
     * Advances the hackathon to ENDED state.
     *
     * @param hackathonId id of the hackathon
     * @param teamName    name of the winning team
     */
    void declareWinner(Long hackathonId, String teamName);

    /**
     * Returns all hackathons in the system.
     *
     * @return list of HackathonResult DTOs
     */
    List<HackathonResult> findAll();

    /**
     * Returns a hackathon by id.
     *
     * @param id id of the hackathon
     * @return the HackathonResult DTO
     */
    HackathonResult findById(Long id);

    /**
     * Advances the hackathon to the next state.
     * Used by the HackathonScheduler (Time actor).
     *
     * @param hackathonId id of the hackathon
     */
    void toNextState(Long hackathonId);

    /**
     * Assigns a role to a staff member for a specific hackathon.
     *
     * @param hackathonId   id of the hackathon
     * @param membroStaffId id of the staff member
     * @param role          role name (e.g. "ORGANIZER", "JUDGE", "MENTOR")
     * @return the created RoleAssignment
     */
    RoleAssignment roleAssign(Long hackathonId, Long membroStaffId, String role);
}