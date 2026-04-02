package unicam.hackhub.domain.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<User> members = new HashSet<>();

    private double balance;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Column(nullable = false)
    private int numMembers;

    public Team() {}

    public Team(String name, User creator) {
        this.name = name;
        this.members = new HashSet<>();
        this.members.add(creator);
        this.balance = 0;
        this.registrationDate = LocalDate.now();
        this.numMembers = 1;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<User> getMembers() { return members; }

    public double getBalance() { return balance; }

    public void addMember(User user) {
        this.members.add(user);
        this.numMembers = this.members.size();
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