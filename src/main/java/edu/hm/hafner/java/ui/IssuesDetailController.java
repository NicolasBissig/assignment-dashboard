package edu.hm.hafner.java.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.java.uc.IssuePropertyDistribution;
import edu.hm.hafner.java.uc.IssuesService;
import edu.hm.hafner.java.uc.IssuesTable;

/**
 * Provides detail information for a specific set of {@link Issue issues}.
 *
 * @author Ullrich Hafner
 */
@Controller
public class IssuesDetailController {
    @SuppressWarnings("InstanceVariableMayNotBeInitialized")
    private final IssuesService issuesService;

    /**
     * Creates a new instance of {@link IssuesDetailController}.
     *
     * @param issuesService
     *         service to access the service layer
     */
    @Autowired
    public IssuesDetailController(final IssuesService issuesService) {
        this.issuesService = issuesService;
    }

    /**
     * AJAX entry point: returns a table with statistics of the uploaded reports (as JSON object). The returned JSON
     * object is in the expected format for the {@code data} property of a DataTable.
     *
     * @return issues statistics of all uploaded reports
     */
    @GetMapping(path = "/ajax/issues", produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by issues.js
    ResponseEntity<?> getIssues() {
        IssuesTable model = issuesService.createIssuesStatistics();

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }

    /**
     * AJAX entry point: returns the number of issues per category (as JSON object). The returned JSON object is in the
     * expected format for the {@code data} property of a bar chart.
     *
     * <p>
     * Example:
     * </p>
     * <pre>
     *     { "labels" : ["Design","Documentation","Best Practices","Performance","Code Style","Error Prone"],
     *      "datasets" : [
     *          {"data" : [15,3,20,6,53,12]}
     *      ]
     *      }
     * </pre>
     * @param tool
     *         the origin of the issues instance to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return the number of issues per category
     */
    @GetMapping(path = "/ajax/categories", produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by details.js
    ResponseEntity<?> getCategories(@RequestParam("tool") final String tool,
            @RequestParam("reference") final String reference) {
        IssuePropertyDistribution model = issuesService.createDistributionByCategory(tool, reference);

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }

    /**
     * Ajax entry point: returns the number of issues per type (as JSON object). The returned JSON object is in the
     * expected format for the {@code data} property of a bar chart.
     *
     * @param tool
     *         the tool of the issues to show the details for
     * @param reference
     *         the reference of the issues instance to show the details for
     *
     * @return the number of issues per type
     */
    @GetMapping(path = "/ajax/types", produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unused")
    // called by details.js
    ResponseEntity<?> getTypes(@RequestParam("tool") final String tool,
            @RequestParam("reference") final String reference) {
        IssuePropertyDistribution model = issuesService.createDistributionByType(tool, reference);

        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(model));
    }
}
