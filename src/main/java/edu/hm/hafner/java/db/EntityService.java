package edu.hm.hafner.java.db;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;

import static java.util.stream.Collectors.*;

/**
 * Service to interact with a database to store and load Issue objects and Issues objects.
 *
 * @author Michael Schmid
 */
@Service
@Transactional
public class EntityService {
    private final IssueRepository issueRepository;
    private final ReportRepository reportRepository;
    private final Mapper mapper;

    @PersistenceContext
    private final EntityManager manager;

    /**
     * Creates a new instance of {@link EntityService}.
     *
     * @param issueRepository
     *         JPA repository to store and load {@link Issue} objects
     * @param reportRepository
     *         JPA repository to store and load {@link Report} objects
     * @param mapper
     *         OR mapper convert dto-object to entity-object and vice versa
     * @param manager
     *         entity manager to use to build custom queries
     */
    @Autowired
    public EntityService(final IssueRepository issueRepository, final ReportRepository reportRepository,
            final Mapper mapper, final EntityManager manager) {
        this.issueRepository = issueRepository;
        this.reportRepository = reportRepository;
        this.mapper = mapper;
        this.manager = manager;
    }

    /**
     * Inserts an {@link Issue} object into the database. Returns a new copy of the object with the values from the
     * database.
     *
     * @param issue
     *         the issue to insert into the database
     *
     * @return new instance of the issue with the values of the database
     */
    public Issue insert(final Issue issue) {
        IssueEntity entity = mapper.map(issue);

        IssueEntity saved = issueRepository.save(entity);

        return mapper.map(saved);
    }

    /**
     * Inserts a {@link Report} object into the database. Returns a new copy of the object with the values from the
     * database.
     *
     * @param report
     *         to report to insert into the database
     *
     * @return new instance of the report with the values of the database
     */
    public Report insert(final Report report) {
        ReportEntity entity = mapper.map(report);

        ReportEntity saved = reportRepository.save(entity);

        return mapper.map(saved);
    }

    /**
     * Selects all issues that are stored in the database.
     *
     * @return set of all issues in the database
     */
    public Set<Issue> selectAllIssues() {
        return issueRepository.findAll().stream().map(mapper::map).collect(toSet());
    }

    /**
     * Selects a single issue identified by the id.
     *
     * @param id
     *         of the desired issue
     *
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Issue> selectIssue(final int id) {
        return issueRepository.findById(id).map(mapper::map);
    }

    /**
     * Selects all reports that are stored in the database.
     *
     * @return set of all reports in the database
     */
    public Set<Report> selectAllReports() {
        return reportRepository.findAll().stream().map(mapper::map).collect(toSet());
    }

    /**
     * Selects a single issue identified by the id.
     *
     * @param id
     *         of the desired issue
     *
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Report> selectReport(final int id) {
        return reportRepository.findById(id).map(mapper::map);
    }

    /**
     * Selects a report for the specified tool ID and report file.
     *
     * @param toolId
     *         ID of the static analysis tool
     * @param originReportFile
     *         ID of report
     *
     * @return the matching ordered list of issues
     */
    public Optional<Report> selectReportByToolIdAndOriginReportFile(final String toolId,
            final String originReportFile) {
        return reportRepository.findByToolIdAndOriginReportFile(toolId, originReportFile).map(mapper::map);

    }

    /**
     * Returns a list of the references of all persisted reports.
     *
     * @return list of references
     */
    public List<String> findAllReferences() {
        TypedQuery<String> query = manager.createQuery(
                "SELECT i.id FROM ReportEntity AS i", String.class);
        return query.getResultList();
    }
}
