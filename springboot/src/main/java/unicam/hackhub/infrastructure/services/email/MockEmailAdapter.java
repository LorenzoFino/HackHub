package unicam.hackhub.infrastructure.services.email;

import org.springframework.stereotype.Component;
import unicam.hackhub.domain.model.Hackathon;
import unicam.hackhub.domain.model.Team;

import java.util.Set;

/**
 * Mock implementation of the external email notification system.
 * In production this would integrate with a real email provider (e.g. SendGrid, SES).
 * Used to notify teams about hackathon modifications and cancellations.
 */
@Component
public class MockEmailAdapter {

    /**
     * Sends a modification notification email to all registered teams.
     *
     * @param teams     set of teams registered to the hackathon
     * @param hackathon the modified hackathon
     */
    public void sendModificationNotification(Set<Team> teams, Hackathon hackathon) {
        if (teams == null || teams.isEmpty()) {
            System.out.println("[EMAIL] No teams to notify for hackathon: " + hackathon.getName());
            return;
        }

        System.out.println("[EMAIL] Sending modification notification for hackathon: " + hackathon.getName());
        for (Team team : teams) {
            team.getMembers().forEach(member ->
                    System.out.println("[EMAIL] Notified " + member.getEmail() +
                            " about changes to hackathon: " + hackathon.getName())
            );
        }
        System.out.println("[EMAIL] Modification notifications sent successfully.");
    }

    /**
     * Sends a cancellation notification email to all registered teams.
     *
     * @param teams     set of teams registered to the hackathon
     * @param hackathon the cancelled hackathon
     */
    public void sendCancellationNotification(Set<Team> teams, Hackathon hackathon) {
        if (teams == null || teams.isEmpty()) {
            System.out.println("[EMAIL] No teams to notify for hackathon: " + hackathon.getName());
            return;
        }

        System.out.println("[EMAIL] Sending cancellation notification for hackathon: " + hackathon.getName());
        for (Team team : teams) {
            team.getMembers().forEach(member ->
                    System.out.println("[EMAIL] Notified " + member.getEmail() +
                            " about cancellation of hackathon: " + hackathon.getName())
            );
        }
        System.out.println("[EMAIL] Cancellation notifications sent successfully.");
    }

    /**
     * Sends a password reset email to the given address.
     *
     * @param email email of the user requesting password reset
     */
    public void sendPasswordResetEmail(String email) {
        System.out.println("[EMAIL] Sending password reset email to: " + email);
    }
}