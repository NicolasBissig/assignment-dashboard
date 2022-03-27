package edu.hm.hafner.java.db;

import javax.persistence.EntityManager;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link EntityService}.
 *
 * @author Michael Schmid
 */
class EntityServiceTest {
    private static final Mapper MAPPER = new Mapper();
    private static final int EXAMPLE_UUID = 1234;
    private static final String EXAMPLE_ORIGIN = "origin";
    private static final String EXAMPLE_REFERENCE = "1";

    private static final Report ISSUES = new Report();
    private static final Issue FIRST_ISSUE = new IssueBuilder().setLineStart(1).build();
    private static final Issue SECOND_ISSUE = new IssueBuilder().setLineStart(2).build();

    static {
        ISSUES.setOrigin(EXAMPLE_ORIGIN, EXAMPLE_REFERENCE);
        ISSUES.add(FIRST_ISSUE);
        ISSUES.add(SECOND_ISSUE);
    }

    @Test
    void selectNotExistingIssueShouldReturnAnEmptyOptional() {
        EntityService sut = createEntityService(mock(IssueRepository.class));

        Optional<Issue> issue = sut.selectIssue(EXAMPLE_UUID);

        assertThat(issue.isPresent()).isFalse();
    }

    @Test
    void selectExistingIssueShouldReturnTheIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        EntityService sut = createEntityService(issueRepository);

        IssueEntity entity = new IssueEntity();
        entity.setId(EXAMPLE_UUID);
        entity.setSeverity(Severity.ERROR.getName());
        when(issueRepository.findById(EXAMPLE_UUID)).thenReturn(Optional.of(entity));

        Optional<Issue> issue = sut.selectIssue(EXAMPLE_UUID);

        assertThat(issue.isPresent()).isTrue();
    }

    @Test
    void selectNotExistingReportShouldReturnAnEmptyOptional() {
        EntityService sut = createEntityService(mock(IssueRepository.class));

        Optional<Report> issue = sut.selectReport(EXAMPLE_UUID);

        assertThat(issue.isPresent()).isFalse();
    }

    @Test
    void selectExistingIssuesShouldReturnTheIssue() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        ReportRepository reportRepository = mock(ReportRepository.class);
        EntityService sut = createEntityService(issueRepository, reportRepository);
        when(reportRepository.findById(EXAMPLE_UUID)).thenReturn(Optional.of(MAPPER.map(ISSUES)));

        Optional<Report> optionalResult = sut.selectReport(EXAMPLE_UUID);

        assertThat(optionalResult.isPresent()).isTrue();
        assertThat(optionalResult.get().getId()).isEqualTo(EXAMPLE_ORIGIN);
        assertThat(optionalResult.get().getName()).isEqualTo(EXAMPLE_REFERENCE);
        assertThat(optionalResult.get().iterator()).toIterable().containsExactly(FIRST_ISSUE, SECOND_ISSUE);
    }

    @Test
    void insertNotExistingIssues() {
        IssueRepository issueRepository = mock(IssueRepository.class);
        ReportRepository reportRepository = mock(ReportRepository.class);
        EntityService sut = createEntityService(issueRepository, reportRepository);
        when(issueRepository.findById(1)).thenReturn(Optional.of(MAPPER.map(FIRST_ISSUE)));
        ReportEntity entity = MAPPER.map(ISSUES);
        when(reportRepository.save(entity)).thenReturn(entity);

        Report saved = sut.insert(ISSUES);
        assertThat(saved).isEqualTo(ISSUES);

        verify(reportRepository, times(1)).save(entity);

        verifyNoMoreInteractions(reportRepository);
        verifyNoMoreInteractions(issueRepository);
    }

    private EntityService createEntityService(final IssueRepository issueRepository) {
        return new EntityService(issueRepository, mock(ReportRepository.class), MAPPER, mock(EntityManager.class));
    }

    private EntityService createEntityService(final IssueRepository issueRepository,
            final ReportRepository reportRepository) {
        return new EntityService(issueRepository, reportRepository, MAPPER, mock(EntityManager.class));
    }
}
