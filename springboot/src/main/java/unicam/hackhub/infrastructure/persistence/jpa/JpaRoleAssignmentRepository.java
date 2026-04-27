package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.RoleAssignment;
import unicam.hackhub.domain.repository.RoleAssignmentRepository;

/**
 * JPA implementation of RoleAssignmentRepository
 */
@Repository
public interface JpaRoleAssignmentRepository extends JpaRepository<RoleAssignment, Long>, RoleAssignmentRepository {
}
