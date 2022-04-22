package edu.hm.hafner.java.uc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.hm.hafner.analysis.Issue;

/**
 * Model that provides the totals for a set of {@link Issue issues}.
 *
 * @author Ullrich Hafner
 * @see <a href="http://www.chartjs.org/docs/latest/charts/bar.html#dataset-properties">Bar Chart Dataset</a>
 */
@SuppressWarnings({"FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"}) // Will be converted to Json
public class IssuePropertyDistribution {
    private final List<String> labels = new ArrayList<>();
    private final List<IssuesSizeList> datasets = new ArrayList<>();

    /**
     * Creates a new instance of {@link IssuePropertyDistribution}.
     *
     * @param counts
     *         a mapping of properties to number of issues
     */
    public IssuePropertyDistribution(final Map<String, Integer> counts) {
        this(counts.entrySet());
    }

    /**
     * Creates a new instance of {@link IssuePropertyDistribution}.
     *
     * @param entries
     *         an array that maps properties to number of issues
     */
    @SafeVarargs
    public IssuePropertyDistribution(Map.Entry<String, Integer>... entries) {
        this(Arrays.asList(entries));
    }

    /**
     * Creates a new instance of {@link IssuePropertyDistribution}.
     *
     * @param entries
     *         a collection that maps properties to number of issues
     */
    public IssuePropertyDistribution(final Collection<Entry<String, Integer>> entries) {
        List<Integer> values = new ArrayList<>();
        for (Entry<String, Integer> entry : entries) {
            labels.add(entry.getKey());
            values.add(entry.getValue());
        }
        datasets.add(new IssuesSizeList(values));
    }
}
