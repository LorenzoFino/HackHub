package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import unicam.hackhub.domain.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Column(nullable = false)
    private String name;

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "team_name", referencedColumnName = "name")
    private Team team;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public boolean hasTeam() {
        return this.team != null;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User user &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}