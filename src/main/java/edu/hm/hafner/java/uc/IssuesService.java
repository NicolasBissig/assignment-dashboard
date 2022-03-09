package edu.hm.hafner.java.uc;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.registry.ParserDescriptor;
import edu.hm.hafner.analysis.registry.ParserRegistry;
import edu.hm.hafner.java.MultipartFileReaderFactory;
import edu.hm.hafner.java.db.EntityService;

/**
 * Provides services for a {@link Report}.
 *
 * @author Ullrich Hafner
 */
@Service
public class IssuesService {
    private final EntityService issuesEntityService;
    private final ParserRegistry parserRegistry = new ParserRegistry();

    /**
     * Creates a new instance of {@link IssuesService}.
     *
     * @param issuesEntityService
     *         service to access the DB layer
     */
    @Autowired
    public IssuesService(final EntityService issuesEntityService) {
        this.issuesEntityService = issuesEntityService;
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
        Optional<Report> issues = issuesEntityService.selectReportByToolIdAndOriginReportFile(toolId, originFileName);
        if (issues.isPresent()) {
            Map<String, Integer> counts = issues.get().getPropertyCount(Issue::getCategory);

            return new IssuePropertyDistribution(counts);
        }
        return new IssuePropertyDistribution(new HashMap<>()); // TODO: exception?
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
        Optional<Report> issues = issuesEntityService.selectReportByToolIdAndOriginReportFile(toolId, originFileName);
        if (issues.isPresent()) {
            Map<String, Integer> counts = issues.get().getPropertyCount(Issue::getType);

            return new IssuePropertyDistribution(counts);
        }
        return new IssuePropertyDistribution(new HashMap<>()); // TODO: exception?
    }

    /**
     * Returns the available static analysis tools.
     *
     * @return all tools
     */
    public List<ParserDescriptor> findAllTools() {
        return parserRegistry.getAllDescriptors();
    }

    /**
     * Parses the specified file with the parser with the given ID.
     *
     * @param tool
     *         ID of the static analysis tool
     * @param file
     *         the file to parse
     * @param id
     *         the ID for this report
     *
     * @return the issues of the specified report
     */
    public Report parse(final String tool, final MultipartFile file, final String id) {
        ParserDescriptor descriptor = parserRegistry.get(tool);
        IssueParser parser = descriptor.createParser();
        Report report = parser.parse(new MultipartFileReaderFactory(file, id, StandardCharsets.UTF_8));
        return issuesEntityService.save(report);
    }

    private InputStreamReader asStream(final InputStream file) {
        return new InputStreamReader(new BOMInputStream(file), StandardCharsets.UTF_8);
    }

    /**
     * Creates a table with the statistics of all issues. Each row shows the statistics of one uploaded report.
     *
     * @return a statistics table
     */
    public IssuesTable createIssuesStatistics() {
        Set<Report> reports = issuesEntityService.selectAllReports();
        IssuesTable statistics = new IssuesTable();
        for (Report report : reports) {
            statistics.addRow(report);
        }
        return statistics;
    }
}
