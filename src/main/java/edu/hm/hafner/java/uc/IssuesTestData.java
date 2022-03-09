package edu.hm.hafner.java.uc;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.stereotype.Component;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.registry.ParserRegistry;
import edu.hm.hafner.java.MultipartFileReaderFactory;
import edu.hm.hafner.java.db.EntityService;

/**
 * Populates the database with test data.
 */
@Component
public class IssuesTestData {
    private static final String TEST_PMD_FILE = "/test/pmd.xml";

    private final EntityService entityService;

    /**
     * Creates a new instance of {@link IssuesTestData}.
     *
     * @param entityService
     *         the entity service to use to store the issues
     */
    @Autowired
    public IssuesTestData(final EntityService entityService) {
        this.entityService = entityService;
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
        return new ParserRegistry().get("pmd")
                .createParser()
                .parse(new MultipartFileReaderFactory(getTestReport(reportFileName), reportFileName,
                        StandardCharsets.UTF_8));
    }

    private FileUrlResource getTestReport(final String fileName) {
        return new FileUrlResource(Objects.requireNonNull(IssuesTestData.class.getResource(fileName)));
    }
}
