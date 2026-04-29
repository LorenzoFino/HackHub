package unicam.hackhub.domain.model;

import jakarta.persistence.*;
import unicam.hackhub.domain.utils.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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