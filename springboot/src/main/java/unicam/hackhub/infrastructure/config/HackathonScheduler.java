package unicam.hackhub.infrastructure.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.repository.HackathonRepository;

import java.util.List;

/**
 * Scheduler that simulates the "Tempo" actor from the Use Case Diagram.
 * Automatically advances hackathon states based on dates:
 * - Chiusura Iscrizioni → SUBSCRIPTION to PROGRESS
 * - Fine Invio Sottomissioni → PROGRESS to EVALUATION
 * - Fine Valutazione → EVALUATION to ENDED
 */
@Component
public class HackathonScheduler {

    private final HackathonRepository hackathonRepository;

    public HackathonScheduler(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    /**
     * Runs every minute and advances hackathon states when dates allow.
     * In production this could run less frequently (e.g. every hour).
     */
    @Scheduled(fixedRate = 1200000)
    public void advanceHackathonStates() {
        List<Hackathon> hackathons = hackathonRepository.findAll();
        for (Hackathon hackathon : hackathons) {
            try {
                hackathon.toNextState();
                hackathonRepository.save(hackathon);
            } catch (Exception e) {
                // State transition not allowed yet — skip silently
            }
        }
    }
}