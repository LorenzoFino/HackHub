package unicam.hackhub.application.hackathon;

import unicam.hackhub.application.dto.command.*;
import unicam.hackhub.application.dto.response.HackathonResult;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.RoleAssignment;
import java.util.List;

public interface HackathonService {
    HackathonResult createHackathon(CreateHackathonCommand command);
    HackathonResult updateHackathon(Long hackathonId, UpdateHackathonCommand command);
    void deleteHackathon(Long hackathonId);
    void addMentor(Long hackathonId, Long mentorId);
    void declareWinner(Long hackathonId, String teamName);
    List<HackathonResult> findAll();
    HackathonResult findById(Long id);
    void toNextState(Long hackathonId);
    RoleAssignment roleAssign(Long hackathonId, Long membroStaffId, String role);
}