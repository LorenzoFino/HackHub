package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.User;
import unicam.hackhub.domain.repository.UserRepository;

/**
 * JPA implementation of UserRepository.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, String>, UserRepository {
}