package edu.hm.hafner.java.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public EntityService(final IssueRepository issueRepository, final ReportRepository reportRepository,
            final Mapper mapper, final EntityManager manager) {
        this.issueRepository = issueRepository;
        this.reportRepository = reportRepository;
        this.mapper = mapper;
        this.manager = manager;
    }

    /**
     * Insert a Issue object to database. If the id of the issue is still in the database an exception occurs. Inserts
     * all related LineRanges if they are not present in the database. Returns a new object with the values of the
     * database.
     *
     * @param issue
     *         to insert into the database
     *
     * @return new instance of the issue with the values of the database
     */
    public Issue insert(final Issue issue) {
        IssueEntity entity = mapper.mapToEntity(issue);
        issueRepository.save(entity);
        return mapper.map(entity);
    }

    /**
     * Insert a Issues object to database. If the id of the report is still in the database an exception occurs. Inserts
     * all related Issue entities if they are not present in the database. Returns a new object with the values of the
     * database.
     *
     * @param report
     *         to insert into the database
     *
     * @return new instance of the report with the values of the database
     */
    public Report insert(final Report report) {
        ReportEntity entity = mapper.mapToEntity(report);
        ReportEntity saved = reportRepository.save(entity);
        return mapper.map(saved);
    }

    /**
     * Select all issue entities stored in the database.
     *
     * @return set of all issue entities in the database.
     */
    public Set<Issue> selectAllIssues() {
        return issueRepository.findAll().stream().map(mapper::map).collect(toSet());
    }

    /**
     * Select a single issue identified by the id.
     *
     * @param id
     *         of the desired issue
     *
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Issue> selectIssue(final UUID id) {
        return issueRepository.findById(id).map(mapper::map);
    }

    /**
     * Select all issues entities stored in the database.
     *
     * @return set of all issues entities in the database.
     */
    public Set<Report> selectAllReports() {
        return reportRepository.findAll().stream().map(mapper::map).collect(toSet());
    }

    /*`*
     * Select a single issue identified by the id.
     *
     * @param id
     *         of the desired issue
     *
     * @return Optional with a new issue if it is present in the database else an empty optional.
     */
    public Optional<Report> selectReport(final UUID id) {
        return reportRepository.findById(id).map(mapper::map);
    }

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
    public Optional<Report> selectReportByToolIdAndOriginReportFile(final String toolId, final String originReportFile) {
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

    public Report save(final Report report) {
        ReportEntity entity = reportRepository.save(mapper.mapToEntity(report));

        return mapper.map(entity);
    }
}
