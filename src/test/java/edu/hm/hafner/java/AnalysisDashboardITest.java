package edu.hm.hafner.java;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.hm.hafner.java.db.EntityService;
import edu.hm.hafner.java.db.IssueRepository;
import edu.hm.hafner.java.db.ReportRepository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EntityScan(basePackages = "edu.hm.hafner.java.db")
@AutoConfigureMockMvc
@SuppressWarnings({"PMD.ImmutableField", "PMD.SignatureDeclareThrowsException"})
class AnalysisDashboardITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityService service;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ReportRepository reportRepository;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
        issueRepository.deleteAll();
    }

    @Test
    void shouldVerifyUploadOfReports() throws Exception {
        assertThat(service.selectAllReports()).isEmpty();
        assertThat(service.selectAllIssues()).isEmpty();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/issues")
                        .file("file", getFileContent())
                        .param("tool", "pmd")
                        .param("reference", "pmd.xml"))
                .andExpect(status().isOk())
                .andExpect(view().name("details"))
                .andExpect(model().attribute("tool", "pmd"))
                .andExpect(model().attribute("reference", "pmd.xml"));

        assertThat(service.selectAllReports()).hasSize(1);
        assertThat(service.selectAllReports().stream().findAny().get().getName()).isEqualTo("PMD");
        assertThat(service.selectAllIssues()).hasSize(109);
        assertThat(service.selectAllIssues().stream().filter(issue -> "Code Style".equals(issue.getCategory())))
                .hasSize(53);
    }

    private byte[] getFileContent() throws IOException {
        return IOUtils.toByteArray(
                Objects.requireNonNull(
                        AnalysisDashboardITest.class.getResource("/test/pmd.xml")));
    }
}