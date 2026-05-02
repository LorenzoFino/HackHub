package unicam.hackhub.application.auth;

import org.springframework.stereotype.Service;
import unicam.hackhub.application.dto.command.LoginCommand;
import unicam.hackhub.application.dto.command.RegisterCommand;
import unicam.hackhub.application.dto.response.TokenResult;
import unicam.hackhub.domain.exception.UserNotFoundException;
import unicam.hackhub.domain.model.Staff;
import unicam.hackhub.domain.model.User;
import unicam.hackhub.domain.repository.StaffRepository;
import unicam.hackhub.domain.repository.UserRepository;
import unicam.hackhub.infrastructure.security.JwtTokenProvider;
import unicam.hackhub.infrastructure.services.email.MockEmailAdapter;

import java.util.Optional;

/**
 * Implementation of AuthService.
 * Handles registration, login and password recovery.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MockEmailAdapter emailAdapter;

    public AuthServiceImpl(UserRepository userRepository,
                           StaffRepository staffRepository,
                           JwtTokenProvider jwtTokenProvider,
                           MockEmailAdapter emailAdapter) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailAdapter = emailAdapter;
    }

    @Override
    public TokenResult register(RegisterCommand command) {
        // Check if email is already in use by a user or staff member
        if (userRepository.findByEmail(command.email()).isPresent())
            throw new IllegalArgumentException("Email already in use: " + command.email());

        if (staffRepository.findByEmail(command.email()).isPresent())
            throw new IllegalArgumentException("Email already in use: " + command.email());

        // Create new user — role is USER by default
        User user = new User(command.name(), command.email(), command.password());
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail(), "USER");
        return new TokenResult(token, "Bearer", user.getEmail(), "USER");
    }

    @Override
    public TokenResult login(LoginCommand command) {
        // Check if the user is a Staff member first
        Optional<Staff> staffOpt = staffRepository.findByEmail(command.email());
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            if (!staff.getPassword().equals(command.password()))
                throw new IllegalArgumentException("Invalid credentials");

            String role = staff.getClass().getSimpleName().toUpperCase(); // ORGANIZER, JUDGE, MENTOR
            String token = jwtTokenProvider.generateToken(staff.getEmail(), role);
            return new TokenResult(token, "Bearer", staff.getEmail(), role);
        }

        // Otherwise check regular users
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new UserNotFoundException(command.email()));

        if (!user.getPassword().equals(command.password()))
            throw new IllegalArgumentException("Invalid credentials");

        String token = jwtTokenProvider.generateToken(user.getEmail(), "USER");
        return new TokenResult(token, "Bearer", user.getEmail(), "USER");
    }

    @Override
    public void forgotPassword(String email) {
        // Check if email belongs to a user or staff member
        boolean userExists = userRepository.findByEmail(email).isPresent()
                || staffRepository.findByEmail(email).isPresent();

        if (!userExists)
            throw new UserNotFoundException(email);

        // Send password reset email via EmailSystem
        emailAdapter.sendPasswordResetEmail(email);
    }
}