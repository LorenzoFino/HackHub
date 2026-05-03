package unicam.hackhub.infrastructure.services.calendar;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Mock implementation of the external Calendar system.
 * In production this would integrate with a real calendar provider.
 * Used by mentors to schedule calls with teams.
 */
@Component
public class MockCalendarAdapter {

    /**
     * Simulates booking a call slot between a mentor and a team.
     *
     * @param mentorEmail email of the mentor
     * @param teamName    name of the team
     * @param date        date of the call
     * @param duration    duration in minutes
     * @return the generated call link
     */
    public String bookSlot(String mentorEmail, String teamName,
                           LocalDate date, Integer duration) {
        if (mentorEmail == null || teamName == null || date == null || duration <= 0) {
            throw new IllegalArgumentException("Invalid booking request");
        }

        String link = "https://meet.hackhub.com/" +
                teamName.toLowerCase() + "-" +
                mentorEmail.split("@")[0] + "-" +
                date;

        System.out.println("[CALENDAR] Slot booked — mentor: " + mentorEmail +
                ", team: " + teamName +
                ", date: " + date +
                ", duration: " + duration + " min" +
                ", link: " + link);

        return link;
    }

    /**
     * Simulates cancelling a previously booked slot.
     *
     * @param callLink link of the call to cancel
     * @return true if cancellation succeeded
     */
    public boolean cancelSlot(String callLink) {
        System.out.println("[CALENDAR] Slot cancelled — link: " + callLink);
        return true;
    }
}