package edu.hm.hafner.java.ui;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;
import edu.hm.hafner.java.uc.IssuesTable;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IssuesDetailController.class)
@SuppressWarnings({"PMD.ImmutableField", "PMD.SignatureDeclareThrowsException"})
class IssuesDetailControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IssuesService service;

    @Test
    void shouldVerifyIssueStatisticsOfAllUploadedReports() throws Exception {
        when(service.createIssuesStatistics()).thenReturn(getIssuesTable());

        mockMvc.perform(get("/ajax/issues"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.size()", Matchers.is(1)))
                .andExpect(content().string(Matchers.containsString("checkstyle.xml")));
    }

    private IssuesTable getIssuesTable() {
        IssuesTable issuesTable = new IssuesTable();
        issuesTable.addRow(new Report("checkstyle", "Checkstyle", "checkstyle.xml"));
        return issuesTable;
    }

    @Test
    void shouldVerifyNumberOfIssuesPerCategory() throws Exception {
        when(service.createDistributionByCategory("tool", "reference"))
                .thenReturn(new IssuePropertyDistribution(Map.entry("Design", 2)));

        mockMvc.perform(get("/ajax/categories").param("tool", "tool").param("reference", "reference"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.labels").isArray())
                .andExpect(jsonPath("$.labels", Matchers.hasItem("Design")))
                .andExpect(jsonPath("$.datasets").isArray())
                .andExpect(jsonPath("$.datasets[0].data").isArray())
                .andExpect(jsonPath("$.datasets[0].data[0]", Matchers.is(2)));
    }

    @Test
    void shouldVerifyNumberOfIssuesPerType() throws Exception {
        when(service.createDistributionByType("tool", "reference"))
                .thenReturn(new IssuePropertyDistribution(Map.entry("Design", 2)));

        mockMvc.perform(get("/ajax/types").param("tool", "tool").param("reference", "reference"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.labels", Matchers.hasItem("Design")))
                .andExpect(jsonPath("$.datasets[0].data[0]", Matchers.is(2)));
    }
}