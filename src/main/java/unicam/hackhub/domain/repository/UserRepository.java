package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.User;

import java.util.Optional;

/**
 * Repository interface for User.
 * Implemented by JpaUserRepository in the infrastructure layer.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}