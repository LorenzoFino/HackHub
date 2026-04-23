package unicam.hackhub.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Judge — staff member who evaluates team submissions.
 * Responsibilities (from spec):
 * - views all submissions for the assigned hackathon
 * - releases a valuation (written judgement + score from 0 to 10)
 */
@Entity
@Table(name = "judge")
public class Judge extends Staff {

    public Judge() {}

    public Judge(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }
}