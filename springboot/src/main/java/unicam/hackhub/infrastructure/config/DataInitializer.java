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

    public DataInitializer(StaffRepository staffRepository,
                           TeamRepository teamRepository,
                           UserRepository userRepository,
                           HackathonRepository hackathonRepository,
                           SupportRequestRepository supportRequestRepository) {
        this.staffRepository = staffRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.hackathonRepository = hackathonRepository;
        this.supportRequestRepository = supportRequestRepository;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Create staff
        Organizer organizer = new Organizer("Mario", "Rossi", "mario@hackhub.com", "password");
        Judge judge = new Judge("Anna", "Bianchi", "anna@hackhub.com", "password");
        Mentor mentor = new Mentor("Luca", "Verdi", "luca@hackhub.com", "password");

        staffRepository.save(organizer);
        staffRepository.save(judge);
        staffRepository.save(mentor);

        // Create users
        User user1 = new User("Giuseppe", "giuseppe@mail.com", "password");
        User user2 = new User("Sara", "sara@mail.com", "password");

        userRepository.save(user1);
        userRepository.save(user2);

        // Create team
        Team team = new Team("TeamAlpha", user1);
        user1.setTeam(team);
        teamRepository.save(team);
        userRepository.save(user1);

        // Create hackathon
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

        // Register team to hackathon
        hackathon.registerTeam(team);

        hackathonRepository.save(hackathon);

        // Create support request
        SupportRequest supportRequest = new SupportRequest(
                "Need help with the project structure",
                LocalDate.now(),
                team,
                hackathon
        );
        supportRequestRepository.save(supportRequest);

        System.out.println("[DataInitializer] Test data loaded successfully");

        System.out.println("[DataInitializer] Test data loaded successfully");
    }
}