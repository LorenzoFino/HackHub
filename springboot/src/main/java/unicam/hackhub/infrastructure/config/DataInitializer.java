package unicam.hackhub.infrastructure.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.*;
import unicam.hackhub.domain.repository.*;
import unicam.hackhub.domain.utils.Period;

import java.time.LocalDate;
import java.util.Set;

/**
 * Initializes the database with test data on startup.
 * Only used for development and testing purposes.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final StaffRepository staffRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final HackathonRepository hackathonRepository;
    private final SupportRequestRepository supportRequestRepository;
    private final InvitationRepository invitationRepository;
    private final ReportRepository reportRepository;
    private final SubmissionRepository submissionRepository;

    public DataInitializer(StaffRepository staffRepository,
                           TeamRepository teamRepository,
                           UserRepository userRepository,
                           HackathonRepository hackathonRepository,
                           SupportRequestRepository supportRequestRepository,
                           InvitationRepository invitationRepository,
                           ReportRepository reportRepository,
                           SubmissionRepository submissionRepository) {
        this.staffRepository = staffRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.hackathonRepository = hackathonRepository;
        this.supportRequestRepository = supportRequestRepository;
        this.invitationRepository = invitationRepository;
        this.reportRepository = reportRepository;
        this.submissionRepository = submissionRepository;;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Create staff members
        Organizer organizer = new Organizer("Mario", "Rossi", "mario@hackhub.com", "password");
        Judge judge = new Judge("Anna", "Bianchi", "anna@hackhub.com", "password");
        Mentor mentor = new Mentor("Luca", "Verdi", "luca@hackhub.com", "password");

        staffRepository.save(organizer);
        staffRepository.save(judge);
        staffRepository.save(mentor);

        // Create users
        // user1 — creator of TeamAlpha, registered to hackathon
        User user1 = new User("Giuseppe", "giuseppe@mail.com", "password");
        // user2 — creator of TeamBeta, not registered to any hackathon (useful for invitation tests)
        User user2 = new User("Sara", "sara@mail.com", "password");
        // user3 — no team, useful for testing invitations
        User user3 = new User("Marco", "marco@mail.com", "password");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // Create TeamAlpha — registered to hackathon
        Team team = new Team("TeamAlpha", user1);
        user1.setTeam(team);
        teamRepository.save(team);
        userRepository.save(user1);

        // Create TeamBeta — not registered to any hackathon
        // Useful for testing: send invitation, leave team, delete team
        Team team2 = new Team("TeamBeta", user2);
        user2.setTeam(team2);
        teamRepository.save(team2);
        userRepository.save(user2);

        // Create hackathon in SUBSCRIPTION state
        // Useful for testing: register team, unregister team
        Hackathon hackathon = new Hackathon(
                "HackHub 2026",
                "A university hackathon",
                "No cheating allowed",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                new Period(LocalDate.now().plusDays(8), LocalDate.now().plusDays(10)),
                "Camerino",
                5,
                1000.0,
                organizer,
                judge,
                Set.of(mentor)
        );

        // Register TeamAlpha to hackathon
        hackathon.registerTeam(team);
        hackathonRepository.save(hackathon);

        // Create hackathon in PROGRESS state
        Hackathon hackathonInProgress = new Hackathon(
                "HackHub Progress 2026",
                "A hackathon currently in progress",
                "No cheating allowed",
                LocalDate.now().minusDays(10),  // registrationOpenDate
                LocalDate.now().plusDays(30),   // registrationDeadline — future
                new Period(LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)),
                "Camerino",
                5,
                2000.0,
                organizer,
                judge,
                Set.of(mentor)
        );

        // Register TeamAlpha first, then advance state to PROGRESS
        hackathonInProgress.registerTeam(team);
        // Force state to PROGRESS manually
        hackathonInProgress.getStatus().setCurrentState(HackathonStatus.StateType.PROGRESS);
        hackathonRepository.save(hackathonInProgress);

        // Add test submission while in PROGRESS
        Submission submission = new Submission(
                "My Project",
                "A great project",
                "https://github.com/test",
                LocalDate.now(),
                team,
                hackathonInProgress
        );
        submissionRepository.save(submission);

        // Force state to EVALUATION manually
        hackathonInProgress.getStatus().setCurrentState(HackathonStatus.StateType.EVALUATION);
        hackathonRepository.save(hackathonInProgress);

        // Create support request for main hackathon
        SupportRequest supportRequest = new SupportRequest(
                "Need help with the project structure",
                LocalDate.now(),
                team,
                hackathon
        );
        supportRequestRepository.save(supportRequest);

        // Create pending invitation — user3 invited to TeamBeta
        // Useful for testing: accept invitation, decline invitation
        Invitation invitation = new Invitation(user3, team2, LocalDate.now());
        invitationRepository.save(invitation);

        // Create pending report for hackathon in PROGRESS
        // Useful for testing: manage violations with and without team exclusion
        Report report = new Report(
                "Team is cheating by using external code",
                LocalDate.now(),
                mentor,
                team,
                hackathonInProgress
        );
        reportRepository.save(report);

        // IT4 — Create hackathon in EVALUATION state
        // Useful for testing: modifica valutazione, proclama vincitore, eroga premio
        Hackathon hackathonInEvaluation = new Hackathon(
                "HackHub Evaluation 2026",
                "A hackathon in evaluation phase",
                "No cheating allowed",
                LocalDate.now().minusDays(20),
                LocalDate.now().plusDays(30),
                new Period(LocalDate.now().minusDays(10), LocalDate.now().minusDays(3)),
                "Camerino",
                5,
                3000.0,
                organizer,
                judge,
                Set.of(mentor)
        );
        hackathonInEvaluation.registerTeam(team);
        hackathonInEvaluation.toNextState(); // SUBSCRIPTION -> PROGRESS
        hackathonInEvaluation.toNextState(); // PROGRESS -> EVALUATION
        hackathonRepository.save(hackathonInEvaluation);

        System.out.println("[DataInitializer] Test data loaded successfully");
    }
}