package unicam.hackhub.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicam.hackhub.domain.model.state.*;
import unicam.hackhub.domain.observer.HackathonObservable;
import unicam.hackhub.domain.observer.HackathonObserver;
import unicam.hackhub.domain.utils.Period;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Core entity of the system.
 * Lifecycle managed via the State Pattern.
 * State changes are broadcasted via the Observer Pattern.
 *
 * Relationships (from Analysis_Diagram):
 * - registered to 0..* Team
 * - assigned to Staff: organizer, judge, mentors
 * - winner: Team (set at proclamation)
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hackathon")
public class Hackathon implements HackathonObservable {

    // =====================================
    // Attributes (from Analysis_Diagram)
    // =====================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String rules;

    @Column(nullable = false)
    private LocalDate registrationOpenDate;

    @Column(nullable = false)
    private LocalDate registrationDeadline;

    @Embedded
    private Period period;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer maxTeamSize;

    private Double prize;

    /**
     * Tracks the current state in the DB.
     * The transient state object is rebuilt from this after every JPA event.
     */
    @Embedded
    private HackathonStatus status = new HackathonStatus();

    // =====================================
    // Relationships
    // =====================================

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "hackathon_team",
            joinColumns = @JoinColumn(name = "hackathon_id"),
            inverseJoinColumns = @JoinColumn(name = "team_name")
    )
    private Set<Team> registeredTeams = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "organizer_id")
    private Staff organizer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "judge_id")
    private Staff judge;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "hackathon_mentors",
            joinColumns = @JoinColumn(name = "hackathon_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private Set<Staff> mentors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_name")
    private Team winner;

    /**
     * Active state object (State Pattern).
     * Not persisted — rebuilt by JPA callbacks on every load/save/update.
     */
    @JsonIgnore
    @Transient
    private HackathonState state;

    /**
     * List of observers (Observer Pattern).
     * Not persisted — registered at runtime by the application layer.
     */
    @JsonIgnore
    @Transient
    private List<HackathonObserver> observers = new ArrayList<>();

    // =====================================
    // Constructor
    // =====================================

    public Hackathon(String name, String description, String rules,
                     LocalDate registrationOpenDate, LocalDate registrationDeadline,
                     Period period, String location, int maxTeamSize,
                     Double prize, Staff organizer, Staff judge, Set<Staff> mentors) {
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.registrationOpenDate = registrationOpenDate;
        this.registrationDeadline = registrationDeadline;
        this.period = period;
        this.location = location;
        this.maxTeamSize = maxTeamSize;
        this.prize = prize;
        this.organizer = organizer;
        this.judge = judge;
        this.mentors = mentors != null ? mentors : new HashSet<>();
        this.status = new HackathonStatus();
        this.registeredTeams = new HashSet<>();
        this.observers = new ArrayList<>();
        initializeState();
    }

    // =====================================
    // Team methods
    // =====================================

    /** Registers a team — delegates validation to the current state */
    public void registerTeam(Team team) {
        this.state.registerTeam(team);
        this.registeredTeams.add(team);
    }

    public boolean hasTeam(Team team) {
        return registeredTeams.contains(team);
    }

    public int getRegisteredTeamCount() {
        return registeredTeams.size();
    }

    /** Removes a team from the hackathon — only allowed in SUBSCRIPTION state */
    public void unregisterTeam(Team team) {
        if (!hasTeam(team))
            throw new IllegalArgumentException("Team is not registered to this hackathon");
        this.registeredTeams.remove(team);
    }

    // =====================================
    // Submission methods
    // =====================================

    public void addSubmission(Team team, Submission submission) {
        this.state.addSubmission(team, submission);
    }

    public void updateSubmission(Team team, Submission submission) {
        this.state.updateSubmission(team, submission);
    }

    // =====================================
    // Valuation methods
    // =====================================

    public void valuateSubmission(String teamName, int vote, String judgement) {
        this.state.valuateSubmission(teamName, vote, judgement);
    }

    public void updateValuation(String teamName, int vote, String judgement) {
        this.state.updateValuation(teamName, vote, judgement);
    }

    // =====================================
    // Winner methods
    // =====================================

    /** Declares the winner — only allowed in EvaluationState */
    public void declareWinner(String teamName) {
        this.state.declareWinner(teamName);
        Team team = registeredTeams.stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamName));
        this.winner = team;
        this.changeState(HackathonStatus.StateType.ENDED);
    }

    // =====================================
    // Staff methods
    // =====================================

    public void addMentor(Staff mentor) {
        this.mentors.add(mentor);
    }

    // =====================================
    // State management (State Pattern)
    // =====================================

    /** Changes the state and notifies all registered observers */
    public void changeState(HackathonStatus.StateType newState) {
        this.status.setCurrentState(newState);
        this.state = HackathonStateFactory.createState(newState, this);
        notifyObservers();
    }

    public void toNextState() {
        this.state.toNextState();
    }

    public HackathonStatus.StateType getCurrentState() {
        return this.status.getCurrentState();
    }

    // =====================================
    // Observer Pattern
    // =====================================

    @Override
    public void addObserver(HackathonObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(HackathonObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (HackathonObserver observer : observers) {
            observer.onStateChanged(this.id, this.status.getCurrentState());
        }
    }

    // =====================================
    // JPA callbacks — rebuild transient fields
    // =====================================

    @PostLoad
    @PostPersist
    @PostUpdate
    private void initializeState() {
        this.state = HackathonStateFactory.createState(
                this.status.getCurrentState(),
                this
        );
        if (this.observers == null) {
            this.observers = new ArrayList<>();
        }
    }
}