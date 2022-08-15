package edu.hm.hafner.java.ui;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.uc.IssuesService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UploadController.class)
@SuppressWarnings({"PMD.ImmutableField", "PMD.SignatureDeclareThrowsException"})
class UploadControllerITest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IssuesService service;

    @Test
    void shouldVerifyUploadOfAStaticAnalysisReport() throws Exception {
        when(service.parse(any(), any(), any())).thenReturn(getReport());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/issues")
                        .file("file", "test".getBytes(StandardCharsets.UTF_8))
                        .param("tool", "checkstyle")
                        .param("reference", "reference"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("details"))
                .andExpect(model().attribute("tool", "checkstyle"))
                .andExpect(model().attribute("reference", "reference"));
    }

    private Report getReport() {
        Report report = new Report("checkstyle", "file", "reference");
        Issue issue = new IssueBuilder()
                .setFileName("myFile.java")
                .setLineStart(10)
                .setLineEnd(10)
                .setMessage("line is too long")
                .build();
        return report.add(issue);

    }
}