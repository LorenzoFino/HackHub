package unicam.hackhub.presentation.api.v1;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicam.hackhub.application.auth.AuthService;
import unicam.hackhub.application.dto.command.LoginCommand;
import unicam.hackhub.application.dto.command.RegisterCommand;
import unicam.hackhub.application.dto.response.TokenResult;
import unicam.hackhub.presentation.dto.request.LoginRequest;
import unicam.hackhub.presentation.dto.request.RegisterRequest;

/**
 * REST controller for authentication and registration.
 * Covers the Visitatore use cases from the sequence diagrams.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** POST /api/v1/auth/register — registers a new user */
    @PostMapping("/register")
    public ResponseEntity<TokenResult> register(@Valid @RequestBody RegisterRequest request) {
        RegisterCommand command = new RegisterCommand(
                request.name(), request.email(), request.password()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(command));
    }

    /** POST /api/v1/auth/login — authenticates a user and returns a JWT token */
    @PostMapping("/login")
    public ResponseEntity<TokenResult> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.email(), request.password());
        return ResponseEntity.ok(authService.login(command));
    }

    /** POST /api/v1/auth/forgot-password — sends a password reset email */
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }
}