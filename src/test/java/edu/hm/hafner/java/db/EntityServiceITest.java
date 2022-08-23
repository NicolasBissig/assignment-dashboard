package edu.hm.hafner.java.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@EntityScan(basePackages = "edu.hm.hafner.java.db")
@Import({EntityService.class, Mapper.class})
@SuppressWarnings({"PMD.ImmutableField", "PMD.SignatureDeclareThrowsException"})
class EntityServiceITest {
    @Autowired
    private EntityService service;

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ReportRepository reportRepository;

    private static final Report REPORT = new Report("reportID", "reportOne", "origin");
    private static final Issue ISSUE = new IssueBuilder().setLineStart(1).setLineEnd(4).build();

    @BeforeEach
    void setUp() {
        issueRepository.deleteAll();
        reportRepository.deleteAll();
    }

    @Test
    void shouldVerifyInsertionOfReportsAndIssuesIntoTheDatabase() {
        assertThat(service.selectAllReports()).isEmpty();
        assertThat(service.selectAllIssues()).isEmpty();

        service.insert(REPORT);
        service.insert(ISSUE);

        assertThat(service.selectAllReports().size()).isEqualTo(1);
        assertThat(service.selectAllReports()).containsOnly(REPORT);
        assertThat(service.selectAllIssues().size()).isEqualTo(1);
        assertThat(service.selectAllIssues()).containsOnly(ISSUE);
    }

    @Test
    void shouldVerifySelectionOfAReportOrAnIssueById() {
        Report report = service.insert(REPORT);
        Issue issue = service.insert(ISSUE);

        int reportId = reportRepository
                .findByToolIdAndOriginReportFile(report.getId(), report.getOriginReportFile()).get().getId();
        int firstIssueId = issueRepository.findAll().get(0).getId();

        assertThat(service.selectReport(reportId)).isNotEmpty().get().isEqualTo(report);
        assertThat(service.selectIssue(firstIssueId)).isNotEmpty().get().isEqualTo(issue);
        assertThat(service.selectReport(100)).isEmpty();
        assertThat(service.selectIssue(100)).isEmpty();
    }

    @Test
    void shouldVerifySelectionOfAReportByToolIdAndOriginReportFile() {
        Report report = service.insert(REPORT);

        assertThat(service.selectReportByToolIdAndOriginReportFile("reportID", "origin"))
                .isNotEmpty().get().isEqualTo(report);
        assertThat(service.selectReportByToolIdAndOriginReportFile("someId", "someOrigin")).isEmpty();
    }

    @Test
    void shouldVerifyFindAllReferences() {
        assertThat(service.findAllReferences()).isEmpty();
        service.insert(REPORT);
        service.insert(new Report("reportId2", "report2"));
        assertThat(service.findAllReferences()).isNotEmpty().hasSize(2);
    }
}