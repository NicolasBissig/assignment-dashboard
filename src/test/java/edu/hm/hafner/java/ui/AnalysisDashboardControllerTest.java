package edu.hm.hafner.java.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import edu.hm.hafner.analysis.registry.ParserDescriptor;
import edu.hm.hafner.analysis.registry.ParserRegistry;
import edu.hm.hafner.java.uc.IssuesService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link AnalysisDashboardController}.
 *
 * @author Lukas Härtinger
 */
class AnalysisDashboardControllerTest {
    @Test
    void shouldCreateUpload() {
        // Given
        Model mockModel = new ConcurrentModel();
        List<ParserDescriptor> notSorted = new ParserRegistry().getAllDescriptors();
        Collections.shuffle(notSorted);
        List<ParserDescriptor> sorted = new ArrayList<>(notSorted);
        sorted.sort(Comparator.comparing(ParserDescriptor::getName));

        //Mock
        IssuesService issuesService = mock(IssuesService.class);
        when(issuesService.findAllTools())
                .thenReturn(notSorted);

        AnalysisDashboardController controller = new AnalysisDashboardController(issuesService);

        //When
        String upload = controller.createUpload(mockModel);

        //Then
        assertThat(upload).isEqualTo("upload");
        assertThat(mockModel.containsAttribute("tools")).as("model should contain attribute 'tools'").isTrue();
        assertThat(mockModel.getAttribute("tools")).isEqualTo(sorted);

    }
}
