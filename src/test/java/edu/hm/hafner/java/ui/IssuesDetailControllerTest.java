package edu.hm.hafner.java.ui;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;
import edu.hm.hafner.java.uc.IssuesTable;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link IssuesDetailController}.
 *
 * @author Ullrich Hafner
 */
class IssuesDetailControllerTest {
    private static final String LABEL = "label";
    private static final String ORIGIN_CATEGORY = "origin-category";
    private static final String REFERENCE_CATEGORY = "reference-category";
    private static final String ORIGIN_TYPE = "origin-type";
    private static final String REFERENCE_TYPE = "reference-type";

    private static final IssuePropertyDistribution EMPTY_DISTRIBUTION = new IssuePropertyDistribution(new HashMap<>());
    private static final IssuePropertyDistribution SINGLETON_DISTRIBUTION = new IssuePropertyDistribution(
            entry(LABEL, 1));

    private static final String LABELS_JSON_PATH = "$.labels";
    private static final String FIRST_DATASET_JSON_PATH = "$.datasets[0].data";

    private static final String REPORT_ID = "REPORT_ID";
    private static final String REPORT_NAME = "REPORT_NAME";
    private static final String REPORT_ORIGIN_FILE = "REPORT_ORIGIN_FILE";

    @Test
    void shouldReturnJsonOfPropertiesDistribution() {
        IssuesService issuesService = mock(IssuesService.class);
        IssuesDetailController controller = new IssuesDetailController(issuesService);

        when(issuesService.createDistributionByCategory(ORIGIN_CATEGORY, REFERENCE_CATEGORY))
                .thenReturn(EMPTY_DISTRIBUTION);
        assertThatResponseIsEmpty(controller.getCategories(ORIGIN_CATEGORY, REFERENCE_CATEGORY));

        when(issuesService.createDistributionByType(ORIGIN_TYPE, REFERENCE_TYPE))
                .thenReturn(EMPTY_DISTRIBUTION);
        assertThatResponseIsEmpty(controller.getTypes(ORIGIN_TYPE, REFERENCE_TYPE));

        when(issuesService.createDistributionByCategory(ORIGIN_CATEGORY, REFERENCE_CATEGORY))
                .thenReturn(SINGLETON_DISTRIBUTION);
        assertThatResponseContainsOneElement(controller.getCategories(ORIGIN_CATEGORY, REFERENCE_CATEGORY));

        when(issuesService.createDistributionByType(ORIGIN_TYPE, REFERENCE_TYPE))
                .thenReturn(SINGLETON_DISTRIBUTION);
        assertThatResponseContainsOneElement(controller.getTypes(ORIGIN_TYPE, REFERENCE_TYPE));
    }

    @Test
    void shouldReturnSampleReport() {
        // Given
        final IssuesTable table = new IssuesTable();
        table.addRow(new Report());

        final IssuesService issuesService = mock(IssuesService.class);
        final IssuesDetailController controller = new IssuesDetailController(issuesService);

        // When
        when(issuesService.createIssuesStatistics()).thenReturn(table);

        // Then
        assertThatResponseContainsReports(controller.getIssues(), new Report());
    }

    @Test
    void shouldReturnMultipleReports() {
        // Given
        final IssuesTable table = new IssuesTable();
        Report report = new Report(REPORT_ID, REPORT_NAME, REPORT_ORIGIN_FILE);
        Report report2 = new Report(REPORT_ID + "2", REPORT_NAME + "2", REPORT_ORIGIN_FILE + "2");
        table.addRow(report);
        table.addRow(report2);

        final IssuesService issuesService = mock(IssuesService.class);
        final IssuesDetailController controller = new IssuesDetailController(issuesService);

        // When
        when(issuesService.createIssuesStatistics()).thenReturn(table);

        // Then
        assertThatResponseContainsReports(controller.getIssues(), report, report2);
    }

    private void assertThatResponseContainsOneElement(final ResponseEntity<?> categories) {
        assertThat(categories.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(categories.getBody()).inPath(LABELS_JSON_PATH).isArray().containsExactly(LABEL);
        assertThatJson(categories.getBody()).inPath(FIRST_DATASET_JSON_PATH).isArray().containsExactly(1);
    }

    private void assertThatResponseIsEmpty(final ResponseEntity<?> empty) {
        assertThat(empty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(empty.getBody()).inPath(LABELS_JSON_PATH).isArray().isEmpty();
        assertThatJson(empty.getBody()).inPath(FIRST_DATASET_JSON_PATH).isArray().isEmpty();
    }

    private String getDataJsonPath(final int index) {
        return "$.data[" + index + "]";
    }

    private void assertThatResponseContainsReports(final ResponseEntity<?> issues, final Report... reports) {
        assertThat(issues.getStatusCode()).isEqualTo(HttpStatus.OK);

        for (int i = 0; i < reports.length; i++) {
            final Report report = reports[i];

            assertThatJson(issues.getBody()).inPath(getDataJsonPath(i))
                    .isArray()
                    .usingDefaultElementComparator()
                    .containsExactly(
                            report.getId(),
                            report.getName(),
                            report.getOriginReportFile(),
                            String.valueOf(report.getSize()),
                            String.valueOf(report.getSizeOf(Severity.ERROR)),
                            String.valueOf(report.getSizeOf(Severity.WARNING_HIGH)),
                            String.valueOf(report.getSizeOf(Severity.WARNING_NORMAL)),
                            String.valueOf(report.getSizeOf(Severity.WARNING_LOW)));
        }
    }
}
