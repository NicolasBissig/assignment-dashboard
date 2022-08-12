package edu.hm.hafner.java.ui;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;
import edu.hm.hafner.java.uc.IssuesTable;

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

    private static final IssuePropertyDistribution EMPTY_DISTRIBUTION
            = new IssuePropertyDistribution(new HashMap<>());
    private static final IssuePropertyDistribution SINGLETON_DISTRIBUTION
            = new IssuePropertyDistribution(entry(LABEL, 1));

    private static final IssuesTable EMPTY_ISSUE_TABLE
            = new IssuesTable();

    private static final Issue ISSUE_HIGH = new IssueBuilder().setMessage("issue-1")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_HIGH)
            .build();
    private static final Issue ISSUE_NORMAL = new IssueBuilder().setMessage("issue-2")
            .setFileName("file-1")
            .setSeverity(Severity.WARNING_NORMAL)
            .build();
    private static final Issue ISSUE_LOW = new IssueBuilder().setMessage("issue-4")
            .setFileName("file-2")
            .setSeverity(Severity.WARNING_LOW)
            .build();

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

    private void assertThatResponseContainsOneElement(final ResponseEntity<?> categories) {
        assertThatResponseIsEqualTo(categories, "{\"labels\":[\"label\"],\"datasets\":[{\"data\":[1]}]}");
    }

    private void assertThatResponseIsEmpty(final ResponseEntity<?> empty) {
        assertThatResponseIsEqualTo(empty, "{\"labels\":[],\"datasets\":[{\"data\":[]}]}");
    }

    private void assertThatResponseIsEqualTo(final ResponseEntity<?> empty, final String s) {
        assertThat(empty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(empty.getBody()).isEqualTo(s);
    }

    @Test
    void shouldReturnEmptyJsonOfIssueTable() {
        // Given
        IssuesService issuesService = mock(IssuesService.class);
        IssuesDetailController controller = new IssuesDetailController(issuesService);
        when(issuesService.createIssuesStatistics()).thenReturn(EMPTY_ISSUE_TABLE);
        // When
        ResponseEntity<?> response = controller.getIssues();
        //Then
        assertThatIssueResponseIsEmpty(response);
    }

    @Test
    void shouldReturnJsonOfIssueTable() {
        // Given
        IssuesService issuesService = mock(IssuesService.class);
        IssuesDetailController controller = new IssuesDetailController(issuesService);

        // Reports
        Report emptyReport = new Report("1", "emptyReport", "path1");
        Report fullReport = new Report("2", "fullReport", "path2");
        fullReport.addAll(ISSUE_HIGH, ISSUE_NORMAL, ISSUE_LOW);
        // Issue Table
        IssuesTable table = new IssuesTable();
        table.addRow(emptyReport);
        table.addRow(fullReport);

        when(issuesService.createIssuesStatistics()).thenReturn(table);
        // When
        ResponseEntity<?> response = controller.getIssues();
        //Then
        assertThatIssueResponseContainsReports(response, emptyReport, fullReport);
    }

    private void assertThatIssueResponseContainsReports(final ResponseEntity<?> issueResponse,
            final Report... reports) {
        assertThat(issueResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        StringBuilder issuesAsStringRepresentation = new StringBuilder();
        for (Report i : reports) {
            issuesAsStringRepresentation.append(
                    String.format("[\"%s\",\"%s\",\"%s\",\"%d\",\"%d\",\"%d\",\"%d\",\"%d\"],", i.getId(), i.getName(),
                            i.getOriginReportFile(),
                            i.getSize(),
                            i.getSizeOf(Severity.ERROR), i.getSizeOf(Severity.WARNING_HIGH),
                            i.getSizeOf(Severity.WARNING_NORMAL), i.getSizeOf(Severity.WARNING_LOW)));
        }
        issuesAsStringRepresentation.deleteCharAt(issuesAsStringRepresentation.length() - 1);
        assertThatResponseIsEqualTo(issueResponse, String.format("{\"data\":[%s]}", issuesAsStringRepresentation));
    }

    private void assertThatIssueResponseIsEmpty(final ResponseEntity<?> issueResponse) {
        assertThat(issueResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatResponseIsEqualTo(issueResponse, "{\"data\":[]}");
    }
}