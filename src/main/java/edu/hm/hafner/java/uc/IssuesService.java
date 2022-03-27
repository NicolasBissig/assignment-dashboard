package edu.hm.hafner.java.uc;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
    private static final ParserRegistry PARSER_REGISTRY = new ParserRegistry();
    private static final String FILENAME_DUMMY = "<<uploaded file>>";

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
        Optional<Report> issues = entityService.selectReportByToolIdAndOriginReportFile(toolId, originFileName);
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
        Optional<Report> issues = entityService.selectReportByToolIdAndOriginReportFile(toolId, originFileName);
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
        return PARSER_REGISTRY.getAllDescriptors();
    }

    /**
     * Parses the specified file with the parser with the given ID.
     *
     * @param tool
     *         ID of the static analysis tool
     * @param file
     *         the file to parse
     * @param reference
     *         a reference to the report, e.g. a URL of the build, a file name, etc.
     *
     * @return a report with the issues of the specified file
     */
    public Report parse(final String tool, final MultipartFile file, final String reference) {
        ParserDescriptor descriptor = PARSER_REGISTRY.get(tool);
        IssueParser parser = descriptor.createParser();
        Report report = parser.parse(getReaderFactory(file));
        report.setOriginReportFile(reference);
        report.setOrigin(descriptor.getId(), descriptor.getName());
        return entityService.insert(report);
    }

    private MultipartFileReaderFactory getReaderFactory(final MultipartFile file) {
        return new MultipartFileReaderFactory(file,
                StringUtils.defaultIfBlank(file.getOriginalFilename(), FILENAME_DUMMY), StandardCharsets.UTF_8);
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
}
