package edu.hm.hafner.java.uc;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.util.InputStreamSourceReaderFactory;
import edu.hm.hafner.java.db.EntityService;

/**
 * Populates the database with test data.
 */
@Component
public class IssuesTestData {
    private static final String TEST_PMD_FILE = "/test/pmd.xml";

    private final EntityService entityService;
    private final IssuesService issuesService;

    /**
     * Creates a new instance of {@link IssuesTestData}.
     *
     * @param entityService
     *         the entity service to use to store the issues
     * @param issuesService
     *         service to access the service layer
     */
    @Autowired
    public IssuesTestData(final EntityService entityService, final IssuesService issuesService) {
        this.entityService = entityService;
        this.issuesService = issuesService;
    }

    /**
     * Populates the database with issues from a dummy PMD file.
     */
    @PostConstruct
    public void storeTestData() {
        entityService.insert(createTestData());
    }

    /**
     * Creates a set of issues. Reads the issues from a predefined PMD file.
     *
     * @return the issues
     */
    public Report createTestData() {
        return createTestData(TEST_PMD_FILE);
    }

    /**
     * Creates a set of issues. Reads the issues from the specified PMD file.
     *
     * @param reportFileName
     *         file name of the PMD report
     *
     * @return the issues
     */
    public Report createTestData(final String reportFileName) {
        return issuesService.parse("pmd", "Initial-Test-Report",
                new InputStreamSourceReaderFactory(getTestReport(reportFileName), "Initial-Test-Report", StandardCharsets.UTF_8));
    }

    private FileUrlResource getTestReport(final String fileName) {
        return new FileUrlResource(Objects.requireNonNull(IssuesTestData.class.getResource(fileName)));
    }
}
