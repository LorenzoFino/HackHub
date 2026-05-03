package unicam.hackhub.application.hackathon;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicam.hackhub.application.dto.command.*;
import unicam.hackhub.application.dto.mapper.HackathonResultMapper;
import unicam.hackhub.application.dto.response.HackathonResult;
import unicam.hackhub.domain.exception.*;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;
import unicam.hackhub.domain.utils.Period;
import unicam.hackhub.infrastructure.services.email.MockEmailAdapter;
import unicam.hackhub.infrastructure.services.payment.MockPaymentAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of HackathonService.
 * Orchestrates domain objects, repositories, external adapters and exception handling.
 */
@Service
public class HackathonServiceImpl implements HackathonService {

    private final HackathonRepository hackathonRepository;
    private final StaffRepository staffRepository;
    private final RoleAssignmentRepository roleAssignmentRepository;
    private final SupportRequestRepository supportRequestRepository;
    private final MockPaymentAdapter paymentAdapter;
    private final MockEmailAdapter emailAdapter;
    private final HackathonResultMapper mapper;
    private final TeamRepository teamRepository;

    public HackathonServiceImpl(HackathonRepository hackathonRepository,
                                StaffRepository staffRepository,
                                RoleAssignmentRepository roleAssignmentRepository,
                                SupportRequestRepository supportRequestRepository,
                                MockPaymentAdapter paymentAdapter,
                                MockEmailAdapter emailAdapter,
                                HackathonResultMapper mapper,
                                TeamRepository teamRepository) {
        this.hackathonRepository = hackathonRepository;
        this.staffRepository = staffRepository;
        this.roleAssignmentRepository = roleAssignmentRepository;
        this.supportRequestRepository = supportRequestRepository;
        this.paymentAdapter = paymentAdapter;
        this.emailAdapter = emailAdapter;
        this.mapper = mapper;
        this.teamRepository = teamRepository;
    }

    @Override
    public HackathonResult createHackathon(CreateHackathonCommand command) {
        Organizer organizer = (Organizer) staffRepository.findById(command.organizerId())
                .orElseThrow(() -> new StaffNotFoundException(command.organizerId()));
        Judge judge = (Judge) staffRepository.findById(command.judgeId())
                .orElseThrow(() -> new StaffNotFoundException(command.judgeId()));
        Mentor mentor = (Mentor) staffRepository.findById(command.mentorId())
                .orElseThrow(() -> new StaffNotFoundException(command.mentorId()));

        Hackathon hackathon = new Hackathon(
                command.name(), command.description(), command.rules(),
                command.registrationOpenDate(), command.registrationDeadline(),
                new Period(command.startDate(), command.endDate()),
                command.location(), command.maxTeamSize(), command.prize(),
                organizer, judge, Set.of(mentor)
        );
        return mapper.toResult(hackathonRepository.save(hackathon));
    }

    @Override
    public void registerTeam(Long hackathonId, String teamName) {
        Hackathon hackathon = getById(hackathonId);
        Team team = teamRepository.findById(teamName)
                .orElseThrow(() -> new TeamNotFoundException(teamName));
        hackathon.registerTeam(team);
        hackathonRepository.save(hackathon);
    }

    @Override
    public HackathonResult updateHackathon(Long hackathonId, UpdateHackathonCommand command) {
        Hackathon hackathon = getById(hackathonId);

        if (hackathon.getCurrentState() != HackathonStatus.StateType.SUBSCRIPTION)
            throw new InvalidHackathonStateException("Hackathon can only be updated during SUBSCRIPTION");

        // Save registered teams before update for email notification
        Set<Team> registeredTeams = new HashSet<>(hackathon.getRegisteredTeams());

        hackathon.setName(command.name());
        hackathon.setDescription(command.description());
        hackathon.setRules(command.rules());
        hackathon.setRegistrationOpenDate(command.registrationOpenDate());
        hackathon.setRegistrationDeadline(command.registrationDeadline());
        hackathon.setPeriod(new Period(command.startDate(), command.endDate()));
        hackathon.setLocation(command.location());
        hackathon.setMaxTeamSize(command.maxTeamSize());
        hackathon.setPrize(command.prize());

        Hackathon saved = hackathonRepository.save(hackathon);

        // Notify registered teams via EmailSystem if any
        if (!registeredTeams.isEmpty())
            emailAdapter.sendModificationNotification(registeredTeams, saved);

        return mapper.toResult(saved);
    }

    @Transactional
    @Override
    public void deleteHackathon(Long hackathonId) {
        Hackathon hackathon = getById(hackathonId);

        if (hackathon.getCurrentState() != HackathonStatus.StateType.SUBSCRIPTION)
            throw new InvalidHackathonStateException("Hackathon can only be deleted during SUBSCRIPTION");

        // Save registered teams before deletion for email notification
        Set<Team> registeredTeams = new HashSet<>(hackathon.getRegisteredTeams());

        // Delete all related support requests to avoid referential integrity violations
        supportRequestRepository.deleteAllByHackathonId(hackathonId);

        // Unregister all teams
        hackathon.getRegisteredTeams().clear();
        hackathonRepository.save(hackathon);

        // Notify teams via EmailSystem if any
        if (!registeredTeams.isEmpty())
            emailAdapter.sendCancellationNotification(registeredTeams, hackathon);

        hackathonRepository.delete(hackathon);
    }

    @Override
    public void addMentor(Long hackathonId, Long mentorId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new HackathonNotFoundException(hackathonId));

        Staff staff = staffRepository.findById(mentorId)
                .orElseThrow(() -> new StaffNotFoundException(mentorId));

        if (!(staff instanceof Mentor mentor))
            throw new InvalidHackathonStateException("Staff member is not a Mentor");

        if (hackathon.getMentors().stream().anyMatch(m -> m.getId().equals(mentorId)))
            throw new MentorAlreadyAssignedException(mentorId);

        hackathon.getMentors().add(mentor);
        hackathonRepository.save(hackathon);
    }

    @Override
    public void declareWinner(Long hackathonId, String teamName) {
        Hackathon hackathon = getById(hackathonId);
        hackathon.declareWinner(teamName);

        // Process prize payment via external payment system
        boolean paymentSuccess = paymentAdapter.processPayment(teamName, hackathon.getPrize());
        if (!paymentSuccess)
            throw new InvalidHackathonStateException("Payment failed for team: " + teamName);

        hackathonRepository.save(hackathon);
    }

    @Override
    public List<HackathonResult> findAll() {
        return hackathonRepository.findAll().stream().map(mapper::toResult).toList();
    }

    @Override
    public HackathonResult findById(Long id) {
        return mapper.toResult(getById(id));
    }

    @Override
    public void toNextState(Long hackathonId) {
        Hackathon hackathon = getById(hackathonId);
        hackathon.toNextState();
        hackathonRepository.save(hackathon);
    }

    @Override
    public RoleAssignment roleAssign(Long hackathonId, Long memberStaffId, String role) {
        Hackathon hackathon = getById(hackathonId);
        Staff staff = staffRepository.findById(memberStaffId)
                .orElseThrow(() -> new StaffNotFoundException(memberStaffId));
        RoleAssignment assignment = new RoleAssignment(role, staff, hackathon);
        return roleAssignmentRepository.save(assignment);
    }

    /** Internal helper to load Hackathon entity by id */
    private Hackathon getById(Long id) {
        return hackathonRepository.findById(id)
                .orElseThrow(() -> new HackathonNotFoundException(id));
    }
}