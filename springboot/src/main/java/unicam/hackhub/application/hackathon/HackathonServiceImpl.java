package unicam.hackhub.application.hackathon;

import org.springframework.stereotype.Service;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.HackathonRepository;
import unicam.hackhub.domain.repository.RoleAssignmentRepository;
import unicam.hackhub.domain.repository.StaffRepository;
import unicam.hackhub.infrastructure.services.email.MockEmailAdapter;
import unicam.hackhub.infrastructure.services.payment.MockPaymentAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of HackathonService.
 * Orchestrates domain objects and repositories.
 */
@Service
public class HackathonServiceImpl implements HackathonService {

    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final MockPaymentAdapter paymentAdapter;
    private final MockEmailAdapter emailAdapter;

    public HackathonServiceImpl(HackathonRepository hackathonRepository,
                                StaffRepository staffRepository,
                                RoleAssignmentRepository roleAssignmentRepository,
                                MockPaymentAdapter paymentAdapter,
                                MockEmailAdapter emailAdapter) {
        this.hackathonRepository = hackathonRepository;
        this.staffRepository = staffRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.paymentAdapter = paymentAdapter;
        this.emailAdapter = emailAdapter;
    }

    @Override
    public Hackathon createHackathon(Hackathon hackathon) {
        return hackathonRepository.save(hackathon);
    }

    @Override
    public Hackathon updateHackathon(Long hackathonId, Hackathon updatedHackathon) {
        Hackathon hackathon = findById(hackathonId);

        if (hackathon.getCurrentState() != HackathonStatus.StateType.SUBSCRIPTION)
            throw new IllegalStateException("Hackathon can only be updated during SUBSCRIPTION");

        // Save registered teams before update for notification
        Set<Team> registeredTeams = new HashSet<>(hackathon.getRegisteredTeams());

        // Update fields
        hackathon.setName(updatedHackathon.getName());
        hackathon.setDescription(updatedHackathon.getDescription());
        hackathon.setRules(updatedHackathon.getRules());
        hackathon.setRegistrationOpenDate(updatedHackathon.getRegistrationOpenDate());
        hackathon.setRegistrationDeadline(updatedHackathon.getRegistrationDeadline());
        hackathon.setPeriod(updatedHackathon.getPeriod());
        hackathon.setLocation(updatedHackathon.getLocation());
        hackathon.setMaxTeamSize(updatedHackathon.getMaxTeamSize());
        hackathon.setPrize(updatedHackathon.getPrize());

        Hackathon saved = hackathonRepository.save(hackathon);

        // Notify registered teams via email if any
        if (!registeredTeams.isEmpty())
            emailAdapter.sendModificationNotification(registeredTeams, saved);

        return saved;
    }

    @Override
    public void deleteHackathon(Long hackathonId) {
        Hackathon hackathon = findById(hackathonId);

        if (hackathon.getCurrentState() != HackathonStatus.StateType.SUBSCRIPTION)
            throw new IllegalStateException("Hackathon can only be deleted during SUBSCRIPTION");

        // Save registered teams before deletion for notification
        Set<Team> registeredTeams = new HashSet<>(hackathon.getRegisteredTeams());

        // Unregister all teams
        hackathon.getRegisteredTeams().clear();
        hackathonRepository.save(hackathon);

        // Notify teams via email if any
        if (!registeredTeams.isEmpty())
            emailAdapter.sendCancellationNotification(registeredTeams, hackathon);

        hackathonRepository.delete(hackathon);
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

        // Process prize payment via external payment system
        boolean paymentSuccess = paymentAdapter.processPayment(teamName, hackathon.getPrize());
        if (!paymentSuccess)
            throw new IllegalStateException("Payment failed for team: " + teamName);

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
    public RoleAssignment roleAssign(Long hackathonId, Long memberStaffId, String role) {
        Hackathon hackathon = findById(hackathonId);

        Staff staff = staffRepository.findById(memberStaffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found: " + memberStaffId));

        RoleAssignment assignment = new RoleAssignment(role, staff, hackathon);
        return roleAssignmentRepository.save(assignment);
    }
}