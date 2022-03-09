package edu.hm.hafner.java.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.java.uc.IssuesService;

/**
 * Uploads new issues reports.
 *
 * @author Ullrich Hafner
 */
@Controller
public class UploadController {
    static final String FILENAME_DUMMY = "<<uploaded file>>";

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
     * <p>
     * Example:
     * <pre>
     *     curl -F "file=@checkstyle-result.xml" -F"tool=checkstyle" https://[id].herokuapp.com/issues
     * </pre>
     *
     * @param file
     *         the analysis report
     * @param tool
     *         the ID of the static analysis tool
     * @param id
     *         a unique ID for the report
     * @param model
     *         UI model, will be filled with {@code origin} and {@code  reference}
     *
     * @return name of the details view
     */
    @RequestMapping(path = "/issues", method = RequestMethod.POST)
    String upload(@RequestParam("file") final MultipartFile file,
            @RequestParam("tool") final String tool,
            @RequestParam("id") final String id,
            final Model model) {
        Report issues = issuesService.parse(tool, file, id);

        model.addAttribute("origin", issues.getId());
        model.addAttribute("reference", issues.getOriginReportFile());

        return "details";
    }
}
