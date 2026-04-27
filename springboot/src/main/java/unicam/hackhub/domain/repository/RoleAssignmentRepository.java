package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.RoleAssignment;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RoleAssignment.
 * Implemented by JpaRoleAssignmentRepository in the infrastructure layer.
 */
public interface RoleAssignmentRepository {

    RoleAssignment save(RoleAssignment assignment);

    Optional<RoleAssignment> findById(Long id);

    List<RoleAssignment> findAllByHackathonId(Long hackathonId);

    List<RoleAssignment> findAllByStaffId(Long staffId);
}
