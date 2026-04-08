package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.Staff;
import unicam.hackhub.domain.repository.StaffRepository;

/**
 * JPA implementation of StaffRepository.
 */
@Repository
public interface JpaStaffRepository extends JpaRepository<Staff, Long>, StaffRepository {
}