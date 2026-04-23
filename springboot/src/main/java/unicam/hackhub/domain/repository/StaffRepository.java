package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Staff;

import java.util.Optional;

/**
 * Repository interface for Staff.
 * Implemented by JpaStaffRepository in the infrastructure layer.
 */
public interface StaffRepository {

    Staff save(Staff staff);

    Optional<Staff> findById(Long id);

    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);
}