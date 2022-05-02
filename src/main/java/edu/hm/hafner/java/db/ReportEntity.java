package edu.hm.hafner.java.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * POJO to store a {@link ReportEntity} to the database.
 */
@Entity
@Table(name = "report")
@SuppressWarnings("PMD.DataClass")
@SuppressFBWarnings("NP")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String toolId;
    private String toolName;
    private String originReportFile;

    @OneToMany(mappedBy = "issues", cascade = CascadeType.ALL)
    private List<IssueEntity> issues;

    ReportEntity(final String toolId, final String toolName, final String originReportFile) {
        this.toolId = toolId;
        this.toolName = toolName;
        this.originReportFile = originReportFile;
        this.issues = new ArrayList<>();
    }

    /**
     * Creates a new instance of {@link ReportEntity}.
     */
    public ReportEntity() {
        this("", "", "");
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(final String toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(final String toolName) {
        this.toolName = toolName;
    }

    public String getOriginReportFile() {
        return originReportFile;
    }

    public void setOriginReportFile(final String originReportFile) {
        this.originReportFile = originReportFile;
    }

    /**
     * Returns the issues of the {@link ReportEntity}.
     *
     * @return the issues
     */
    public List<IssueEntity> getIssues() {
        return issues;
    }

    /**
     * Setter to set the issues of the {@link ReportEntity}.
     *
     * @param issues
     *         the issues
     */
    public void setIssues(final List<IssueEntity> issues) {
        this.issues = issues;
    }

    /**
     * Adds a {@link IssueEntity} tho the {@link ReportEntity}.
     *
     * @param issueEntity
     *         the {@link IssueEntity}
     *
     * @return the added {@link IssueEntity}
     */
    public IssueEntity addIssueEntity(final IssueEntity issueEntity) {
        getIssues().add(issueEntity);
        issueEntity.setIssues(this);

        return issueEntity;
    }

    @Override
    public boolean equals(@CheckForNull final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReportEntity that = (ReportEntity) o;
        return id == that.id && Objects.equals(toolId, that.toolId) && Objects.equals(toolName,
                that.toolName) && Objects.equals(originReportFile, that.originReportFile)
                && Objects.equals(issues, that.issues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, toolId, toolName, originReportFile, issues);
    }
}
