package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.List;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Model that provides the different sizes of a set of {@link Issue issues}.
 *
 * @author Ullrich Hafner
 * @see <a href="http://www.chartjs.org/docs/latest/charts/bar.html#dataset-properties">Bar Chart Dataset</a>
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
public class IssuesTable {
    private final List<List<String>> data = new ArrayList<>();

    /**
     * Adds a new row to this table.
     *
     * @param issues
     *         the set of issues that should be added
     */
    public void addRow(final Report issues) {
        List<String> row = new ArrayList<>();
        row.add(issues.getId());
        row.add(issues.getName());
        row.add(String.valueOf(issues.getSize()));
        row.add(String.valueOf(issues.getSizeOf(Severity.ERROR)));
        row.add(String.valueOf(issues.getSizeOf(Severity.WARNING_HIGH)));
        row.add(String.valueOf(issues.getSizeOf(Severity.WARNING_NORMAL)));
        row.add(String.valueOf(issues.getSizeOf(Severity.WARNING_LOW)));

        data.add(row);
    }
}
