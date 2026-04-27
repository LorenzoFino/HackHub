package unicam.hackhub.application.hackathon;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Mentor;
import unicam.hackhub.domain.model.RoleAssignment;
import unicam.hackhub.domain.model.Staff;
import unicam.hackhub.domain.repository.HackathonRepository;
import unicam.hackhub.domain.repository.RoleAssignmentRepository;
import unicam.hackhub.domain.repository.StaffRepository;

import java.util.List;

/**
 * Implementation of HackathonService.
 * Orchestrates domain objects and repositories.
 */
@Service
public class HackathonServiceImpl implements HackathonService {

    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;

    public HackathonServiceImpl(HackathonRepository hackathonRepository,
                                StaffRepository staffRepository,
                                RoleAssignmentRepository roleAssignmentRepository) {
        this.hackathonRepository = hackathonRepository;
        this.staffRepository = staffRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
    }

    @Override
    public Hackathon createHackathon(Hackathon hackathon) {
        return hackathonRepository.save(hackathon);
    }

    @Override
    public void addMentor(Long hackathonId, Long mentorId) {
        Hackathon hackathon = findById(hackathonId);

        Mentor mentor = (Mentor) staffRepository.findById(mentorId)
                .orElseThrow(() -> new IllegalArgumentException("Mentor not found: " + mentorId));

        hackathon.addMentor(mentor);
        hackathonRepository.save(hackathon);
    }

    @Override
    public void declareWinner(Long hackathonId, String teamName) {
        Hackathon hackathon = findById(hackathonId);
        hackathon.declareWinner(teamName);
        hackathonRepository.save(hackathon);
    }

    @Override
    public List<Hackathon> findAll() {
        return hackathonRepository.findAll();
    }

    @Override
    public Hackathon findById(Long id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found: " + id));
    }

    @Override
    public void toNextState(Long hackathonId) {
        Hackathon hackathon = findById(hackathonId);
        hackathon.toNextState();
        hackathonRepository.save(hackathon);
    }

    @Override
    public RoleAssignment roleAssign(Long hackathonId, Long membroStaffId, String role) {
        Hackathon hackathon = findById(hackathonId);

        Staff staff = staffRepository.findById(membroStaffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found: " + membroStaffId));

        RoleAssignment assignment = new RoleAssignment(role, staff, hackathon);
        return roleAssignmentRepository.save(assignment);
    }
}