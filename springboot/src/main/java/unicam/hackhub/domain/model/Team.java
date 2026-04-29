package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Setter
    @Getter
    @Id
    @Column(nullable = false, unique = true)
    private String name;

    @Getter
    @OneToMany(mappedBy = "team", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<User> members = new HashSet<>();

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_email")
    private User creator;

    @Setter
    @Getter
    private double balance;

    @Setter
    @Getter
    @Column(nullable = false)
    private LocalDate registrationDate;

    @Setter
    @Getter
    @Column(nullable = false)
    private int numMembers;

    public Team() {}

    public Team(String name, User creator) {
        this.name = name;
        this.creator = creator;
        this.members = new HashSet<>();
        this.members.add(creator);
        this.balance = 0;
        this.registrationDate = LocalDate.now();
        this.numMembers = 1;
    }

    public void addMember(User user) {
        this.members.add(user);
        this.numMembers = this.members.size();
    }

    public void removeMember(User user) {
        this.members.remove(user);
        this.numMembers = this.members.size();
    }

    public boolean isCreator(String email) {
        return creator != null && creator.getEmail().equals(email);
    }

    public void increaseBalance(double amount) {
        this.balance += amount;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Team team &&
                Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}