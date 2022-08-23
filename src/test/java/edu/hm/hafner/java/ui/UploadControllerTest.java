package edu.hm.hafner.java.ui;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.uc.IssuesService;
import edu.hm.hafner.java.util.InputStreamSourceReaderFactory;

import static org.mockito.Mockito.*;

/**
 * Tests the class {@link UploadController}.
 *
 * @author Lukas Härtinger
 */
class UploadControllerTest {
    @Test
    void shouldUploadReport() {
        // Given
        MultipartFile mockFile = new MockMultipartFile("name", "filename", "contentType", (byte[]) null);
        Model mockModel = new ConcurrentModel();
        Report emptyReport = new Report("1", "emptyReport", "originReportFile");
        //Mock
        IssuesService issuesService = mock(IssuesService.class);
        when(issuesService.parse(eq("tool"), eq("reference"), any(InputStreamSourceReaderFactory.class)))
                .thenReturn(emptyReport);

        UploadController controller = new UploadController(issuesService);

        //When
        String details = controller.upload(mockFile, "tool", "reference", mockModel);

        //Then
        assertThat(details).isEqualTo("details");
        assertThat(mockModel.containsAttribute("tool")).as("model should contain attribute 'tool'").isTrue();
        assertThat(mockModel.getAttribute("tool")).isEqualTo(emptyReport.getId());
        assertThat(mockModel.containsAttribute("reference")).as("model should contain attribute 'reference'").isTrue();
        assertThat(mockModel.getAttribute("reference")).isEqualTo(emptyReport.getOriginReportFile());

    }
}
