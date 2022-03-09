package edu.hm.hafner.java.db;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to access IssuesEntities.
 *
 * @author Michael Schmid
 */
public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {
    /**
     * Selects all issues with the specified reference. The matching issues will be ordered by origin.
     *
     * @param toolId
     *         ID of the static analysis tool
     * @param originReportFile
     *         ID of report
     *
     * @return the matching ordered list of issues
     */
    Optional<ReportEntity> findByToolIdAndOriginReportFile(String toolId, String originReportFile);
}
