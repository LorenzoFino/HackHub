package unicam.hackhub.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Mentor — staff member who supports teams during the hackathon.
 * Responsibilities (from spec):
 * - views support requests from teams
 * - can propose a call (managed via external Calendar system)
 * - can report a team to the organizer for rule violations
 */
@Entity
@Table(name = "mentor")
public class Mentor extends Staff {

    public Mentor() {}

    public Mentor(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }
}