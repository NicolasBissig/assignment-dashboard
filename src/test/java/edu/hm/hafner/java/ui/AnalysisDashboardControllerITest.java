package edu.hm.hafner.java.ui;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.hm.hafner.analysis.registry.ParserRegistry;
import edu.hm.hafner.java.uc.IssuesService;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AnalysisDashboardController.class)
@SuppressWarnings({"PMD.ImmutableField", "PMD.SignatureDeclareThrowsException"})
class AnalysisDashboardControllerITest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IssuesService service;

    @Test
    void shouldVerifyEntryPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("index"));
    }

    @Test
    void shouldVerifyDetailsEndPoint() throws Exception {
        mockMvc.perform(get("/details").param("tool", "tool").param("reference", "reference"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("details"))
                .andExpect(model().attributeExists("tool", "reference"));
    }

    @Test
    void shouldVerifyDetailsEndPointMissingParams() throws Exception {
        String errorMessage = mockMvc.perform(get("/details"))
                .andExpect(status().is(400))
                .andReturn().getResponse()
                .getErrorMessage();
        assertThat(errorMessage).isEqualTo(
                "Required request parameter 'tool' for method parameter type String is not present");

        String errorMessage2 = mockMvc.perform(get("/details").param("tool", ""))
                .andExpect(status().is(400))
                .andReturn().getResponse()
                .getErrorMessage();
        assertThat(errorMessage2).isEqualTo(
                "Required request parameter 'reference' for method parameter type String is not present");
    }

    @Test
    void shouldVerifyUploadedReports() throws Exception {
        mockMvc.perform(get("/issues"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("issues"));
    }

    @Test
    void shouldVerifyUploadingOfNewReport() throws Exception {
        when(service.findAllTools()).thenReturn(new ArrayList<>(new ParserRegistry().getAllDescriptors()));

        mockMvc.perform(get("/upload"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("upload"))
                .andExpect(model().attributeExists("tools"))
                .andExpect(model().attribute("tools", service.findAllTools()));
    }
}
