package edu.hm.hafner.java.ui;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.uc.IssuesService;
import edu.hm.hafner.java.util.InputStreamSourceReaderFactory;

/**
 * Uploads new issues reports.
 *
 * @author Ullrich Hafner
 */
@Controller
public class UploadController {
    private static final String FILENAME_DUMMY = "<<uploaded file>>";

    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesService issuesService;

    /**
     * Creates a new instance of {@link UploadController}.
     *
     * @param issuesService
     *         service to access the service layer
     */
    @Autowired
    public UploadController(final IssuesService issuesService) {
        this.issuesService = issuesService;
    }

    /**
     * Uploads a static analysis report via curl or a web form.
     *
     * <p>
     * Example:
     * </p>
     * <pre>
     *     curl -F "file=@checkstyle-result.xml" -F"tool=checkstyle" https://[id].herokuapp.com/issues
     * </pre>
     *
     * @param file
     *         the analysis report
     * @param tool
     *         the ID of the static analysis tool
     * @param reference
     *         an optional reference to the report, e.g. a URL of the build, etc. If left empty, then the filename will
     *         be used as reference
     * @param model
     *         UI model, will be filled with {@code tool} and {@code  reference}
     *
     * @return name of the details view
     */
    @PostMapping(path = "/issues")
    String upload(@RequestParam("file") final MultipartFile file,
            @RequestParam("tool") final String tool,
            @RequestParam(value = "reference", required = false) final String reference,
            final Model model) {
        Report report = issuesService.parse(tool, StringUtils.defaultIfBlank(reference, file.getOriginalFilename()),
                getReaderFactory(file));
        issuesService.save(report);

        model.addAttribute("tool", report.getId());
        model.addAttribute("reference", report.getOriginReportFile());

        return "details";
    }

    private InputStreamSourceReaderFactory getReaderFactory(final MultipartFile file) {
        return new InputStreamSourceReaderFactory(file,
                StringUtils.defaultIfBlank(file.getOriginalFilename(), FILENAME_DUMMY), StandardCharsets.UTF_8);
    }
}
