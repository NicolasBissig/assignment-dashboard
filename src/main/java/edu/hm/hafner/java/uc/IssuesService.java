package edu.hm.hafner.java.uc;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.registry.ParserDescriptor;
import edu.hm.hafner.analysis.registry.ParserRegistry;
import edu.hm.hafner.java.db.EntityService;
import edu.hm.hafner.java.util.InputStreamSourceReaderFactory;
import edu.hm.hafner.util.NoSuchElementException;

/**
 * Provides services for a {@link Report}.
 *
 * @author Ullrich Hafner
 */
@Service
public class IssuesService {
    private static final ParserRegistry PARSER_REGISTRY = new ParserRegistry();

    private final EntityService entityService;

    /**
     * Creates a new instance of {@link IssuesService}.
     *
     * @param entityService
     *         service to access the DB layer
     */
    @Autowired
    public IssuesService(final EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * Returns the number of issues per category.
     *
     * @param toolId
     *         the origin of the issues instance to show the details for
     * @param originFileName
     *         the reference of the issues instance to show the details for
     *
     * @return number of issues per category
     */
    public IssuePropertyDistribution createDistributionByCategory(final String toolId, final String originFileName) {
        return getPropertyDistribution(toolId, originFileName, Issue::getCategory);
    }

    /**
     * Returns the number of issues per type.
     *
     * @param toolId
     *         the origin of the issues instance to show the details for
     * @param originFileName
     *         the reference of the issues instance to show the details for
     *
     * @return number of issues per type
     */
    public IssuePropertyDistribution createDistributionByType(final String toolId, final String originFileName) {
        return getPropertyDistribution(toolId, originFileName, Issue::getType);
    }

    private IssuePropertyDistribution getPropertyDistribution(final String toolId, final String originFileName,
            final Function<Issue, String> propertiesMapper) {
        Optional<Report> issues = entityService.selectReportByToolIdAndOriginReportFile(toolId, originFileName);
        if (issues.isPresent()) {
            Map<String, Integer> counts = issues.get().getPropertyCount(propertiesMapper);

            return new IssuePropertyDistribution(counts);
        }
        throw new NoSuchElementException("No report with origin %s and filename %s", originFileName, toolId);
    }

    /**
     * Returns the available static analysis tools.
     *
     * @return all tools
     */
    public List<ParserDescriptor> findAllTools() {
        return PARSER_REGISTRY.getAllDescriptors();
    }

    /**
     * Parses the specified file with the parser with the given ID.
     *
     * @param tool
     *         ID of the static analysis tool
     * @param reference
     *         a reference to the report, e.g. a URL of the build, a file name, etc.
     * @param readerFactory
     *         the reader factory to read the issues from
     *
     * @return a report with the issues of the specified file
     */
    public Report parse(final String tool, final String reference, final InputStreamSourceReaderFactory readerFactory) {
        ParserDescriptor descriptor = PARSER_REGISTRY.get(tool);
        IssueParser parser = descriptor.createParser();

        Report report = parser.parse(readerFactory);
        report.setOriginReportFile(reference);
        report.setOrigin(descriptor.getId(), descriptor.getName());
        return report;
    }

    /**
     * Creates a table with the statistics of all issues. Each row shows the statistics of one uploaded report.
     *
     * @return a statistics table
     */
    public IssuesTable createIssuesStatistics() {
        Set<Report> reports = entityService.selectAllReports();
        IssuesTable statistics = new IssuesTable();
        for (Report report : reports) {
            statistics.addRow(report);
        }
        return statistics;
    }

    /**
     * Saves the {@link Report} in the database.
     *
     * @param report
     *         to report to save in the database
     */
    public void save(final Report report) {
        entityService.insert(report);
    }
}
