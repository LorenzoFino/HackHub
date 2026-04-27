package unicam.hackhub.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicam.hackhub.domain.model.Report;
import unicam.hackhub.domain.repository.ReportRepository;

/**
 * JPA implementation of ReportRepository.
 */
@Repository
public interface JpaReportRepository extends JpaRepository<Report, Long>, ReportRepository {
}
