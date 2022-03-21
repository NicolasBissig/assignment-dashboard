package edu.hm.hafner.java.db;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link Mapper} for {@link Issue} instances.
 *
 * @author Michael Schmid
 */
class IssuesMapperTest {
    private static final Issue HIGH = new IssueBuilder().setMessage("issue-1")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_HIGH)
            .build();
    private static final Issue NORMAL_1 = new IssueBuilder().setMessage("issue-2")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_NORMAL)
            .build();
    private static final Issue NORMAL_2 = new IssueBuilder().setMessage("issue-3")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_NORMAL)
            .build();
    private static final Issue LOW_2_A = new IssueBuilder().setMessage("issue-4")
            .setFileName("file-2")
            .setSeverity(Severity.WARNING_LOW)
            .build();
    private static final Issue LOW_2_B = new IssueBuilder().setMessage("issue-5")
            .setFileName("file-2")
            .setSeverity(Severity.WARNING_LOW)
            .build();
    private static final Issue LOW_FILE_3 = new IssueBuilder().setMessage("issue-6")
            .setFileName("file-3")
            .setSeverity(Severity.WARNING_LOW)
            .build();

    private static final String ID = "id";

    private static final Report ISSUES = new Report(ID, "Name").addAll(HIGH, NORMAL_1, NORMAL_2, LOW_2_A, LOW_2_B, LOW_FILE_3);

    @Test
    void mapIssuesToReportEntity() {
        Mapper mapper = new Mapper();

        ReportEntity result = mapper.mapToEntity(ISSUES);

        SoftAssertions softly = new SoftAssertions();
        assertIssuesAndEntityEqual(softly, result, ISSUES);
        softly.assertAll();
    }

    @Test
    void issuesRoundTrip() {
        Mapper mapper = new Mapper();

        ReportEntity entity = mapper.mapToEntity(ISSUES);

        SoftAssertions softly = new SoftAssertions();
        assertIssuesAndEntityEqual(softly, entity, ISSUES);

        Report result = mapper.map(entity);

        assertRoundTrip(softly, result, ISSUES);
        softly.assertAll();
    }

    private void assertIssuesAndEntityEqual(final SoftAssertions softly, final ReportEntity entity, final Report issues) {
        softly.assertThat(entity.getToolName()).isEqualTo(issues.getName());
        softly.assertThat(entity.getOriginReportFile()).isEqualTo(issues.getOriginReportFile());
    }

    private void assertRoundTrip(final SoftAssertions softly, final Report result, final Report expected) {
        softly.assertThat(result.getName()).isEqualTo(expected.getName());
        softly.assertThat(result.getSizeOf(Severity.WARNING_LOW)).isEqualTo(expected.getSizeOf(Severity.WARNING_LOW));
        softly.assertThat(result.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(expected.getSizeOf(Severity.WARNING_NORMAL));
        softly.assertThat(result.getSizeOf(Severity.WARNING_HIGH)).isEqualTo(expected.getSizeOf(Severity.WARNING_HIGH));
        softly.assertThat(result.stream().count()).isEqualTo((int)expected.stream().count());

        assertThat(result.get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected.get(0));
    }
}
