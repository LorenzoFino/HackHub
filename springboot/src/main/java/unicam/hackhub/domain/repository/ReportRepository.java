package unicam.hackhub.domain.repository;

import unicam.hackhub.domain.model.Report;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Report (violation reports).
 * Implemented by JpaReportRepository in the infrastructure layer.
 */
public interface ReportRepository {

    Optional<Report> findById(Long id);
    Report save(Report report);
    List<Report> findAllByHackathonId(Long hackathonId);
}
