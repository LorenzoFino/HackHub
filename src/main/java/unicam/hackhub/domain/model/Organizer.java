package unicam.hackhub.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Organizer — staff member who creates hackathons and declares the winner.
 * Responsibilities (from spec):
 * - creates a new hackathon with all required info
 * - can add mentors to a hackathon after creation
 * - declares the winning team (only after all submissions are evaluated)
 */
@Entity
@Table(name = "organizer")
public class Organizer extends Staff {

    public Organizer() {}

    public Organizer(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }
}