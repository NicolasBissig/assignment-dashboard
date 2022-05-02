package edu.hm.hafner.java.ui;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.hm.hafner.analysis.registry.ParserDescriptor;
import edu.hm.hafner.java.uc.IssuesService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Entry point for all direct web requests. Refer to {@link IssuesDetailController} in order to see the Ajax entry
 * points.
 *
 * @author Ullrich Hafner
 */
@Controller
@SuppressFBWarnings("SPRING_ENDPOINT")
public class AnalysisDashboardController {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesService issuesService;

    /**
     * Creates a new instance of {@link AnalysisDashboardController}.
     *
     * @param issuesService
     *         service to access the service layer
     */
    @Autowired
    public AnalysisDashboardController(final IssuesService issuesService) {
        this.issuesService = issuesService;
    }

    /**
     * Returns the main page, served as "index.html".
     *
     * @return the main page
     */
    @GetMapping("/")
    String index() {
        return "index";
    }

    /**
     * Shows the details for one static analysis run.
     *
     * @param tool
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     * @param model
     *         UI model, will be filled with {@code tool} and {@code  reference}
     *
     * @return the URL for the details page
     */
    @GetMapping("/details")
    String createDetails(@RequestParam("tool") final String tool,
            @RequestParam("reference") final String reference, final Model model) {
        model.addAttribute("tool", tool);
        model.addAttribute("reference", reference);

        return "details";
    }

    /**
     * Shows a table with the uploaded reports.
     *
     * @return the URL for the report statistics page
     */
    @GetMapping("/issues")
    String createIssues() {
        return "issues";
    }

    /**
     * Shows a form to upload a new report.
     *
     * @param model
     *         UI model, will be filled with {@code tools}
     *
     * @return the URL for the upload page
     */
    @GetMapping("/upload")
    String createUpload(final Model model) {
        List<ParserDescriptor> allTools = issuesService.findAllTools();
        allTools.sort(Comparator.comparing(ParserDescriptor::getName));
        model.addAttribute("tools", allTools);
        return "upload";
    }
}

