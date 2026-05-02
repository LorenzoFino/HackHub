package unicam.hackhub.application.auth;

import unicam.hackhub.application.dto.command.LoginCommand;
import unicam.hackhub.application.dto.command.RegisterCommand;
import unicam.hackhub.application.dto.response.TokenResult;

/**
 * Application service for authentication and registration.
 * Covers the Visitatore use cases: registration, authentication, recover credentials.
 */
public interface AuthService {

    /**
     * Registers a new user in the system.
     * The user becomes an Authenticated User after registration.
     *
     * @param command contains name, email, password
     * @return TokenResult with JWT token and user info
     */
    TokenResult register(RegisterCommand command);

    /**
     * Authenticates a user and returns a JWT token.
     * The system recognizes the role (USER or STAFF) from the credentials.
     *
     * @param command contains email, password
     * @return TokenResult with JWT token and user info
     */
    TokenResult login(LoginCommand command);

    /**
     * Sends a password reset email to the given address.
     *
     * @param email email of the user requesting password reset
     */
    void forgotPassword(String email);
}
