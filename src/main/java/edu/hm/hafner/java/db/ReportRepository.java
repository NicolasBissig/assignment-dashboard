package edu.hm.hafner.java.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository to access {@link ReportEntity report entities}.
 *
 * @author Michael Schmid
 */
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
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
