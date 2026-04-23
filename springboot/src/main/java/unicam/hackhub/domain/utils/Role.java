package unicam.hackhub.domain.utils;

/**
 * Roles assigned to registered users.
 */
public enum Role {
    USER,        // registered user, can create or join a team
    TEAM_MEMBER  // user who belongs to a team registered to a hackathon
}