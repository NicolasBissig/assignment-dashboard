package edu.hm.hafner.java.db;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * The {@link Mapper} is handling the mapping from DTO to Entity and visa versa. Enables the conversion from a
 * {@link Report} to a {@link ReportEntity} and visa versa. Enables the conversion from a {@link Issue} to a
 * {@link IssueEntity} and visa versa.
 *
 * @author Deniz Mardin
 */
@Service
public class Mapper {
     /**
     * Converts {@link ReportEntity} to a {@link Report}.
     *
     * @param reportEntity
     *         the {@link ReportEntity}
     *
     * @return the converted {@link Report}
     */
    public Report map(final ReportEntity reportEntity) {
        List<Issue> issues = reportEntity.getIssues().stream()
                .map(this::map)
                .collect(Collectors.toList());

        return new Report(reportEntity.getToolId(), reportEntity.getToolName(), reportEntity.getOriginReportFile()).addAll(issues);
    }

    /**
     * Converts {@link Report} to a {@link ReportEntity}.
     *
     * @param report
     *         the {@link Report}
     *
     * @return the converted {@link ReportEntity}
     */
    public ReportEntity mapToEntity(final Report report) {
        ReportEntity reportEntity = new ReportEntity(report.getId(), report.getName(), report.getOriginReportFile());
        report.forEach(issue -> {
            IssueEntity issueEntity = mapToEntity(issue);
            reportEntity.addIssueEntity(issueEntity);
        });

        return reportEntity;
    }

    /**
     * Converts a {@link Issue} to a {@link IssueEntity}.
     *
     * @param issue
     *         the {@link Issue}
     *
     * @return the converted {@link IssueEntity}
     */
    public IssueEntity mapToEntity(final Issue issue) {
        return new IssueEntity(
                issue.getId(),
                issue.getColumnStart(),
                issue.getColumnEnd(),
                issue.getLineStart(),
                issue.getLineEnd(),
                issue.getCategory(),
                issue.getDescription(),
                issue.getFileName(),
                issue.getFingerprint(),
                issue.getMessage(),
                issue.getModuleName(),
                issue.getOrigin(),
                issue.getOriginName(),
                issue.getPackageName(),
                issue.getReference(),
                issue.getSeverity().getName(),
                issue.getType()
        );
    }

    /**
     * Converts a {@link IssueEntity} to a {@link Issue}.
     *
     * @param issueEntity
     *         the {@link IssueEntity}
     *
     * @return the converted {@link Issue}
     */
    public Issue map(final IssueEntity issueEntity) {
        return getIssue(
                issueEntity.getId(),
                issueEntity.getCategory(),
                issueEntity.getColumnEnd(),
                issueEntity.getColumnStart(),
                issueEntity.getDescription(),
                issueEntity.getFileName(),
                issueEntity.getFingerprint(),
                issueEntity.getLineEnd(),
                issueEntity.getLineStart(),
                issueEntity.getMessage(),
                issueEntity.getModuleName(),
                issueEntity.getOrigin(),
                issueEntity.getOriginName(),
                issueEntity.getPackageName(),
                issueEntity.getReference(),
                issueEntity.getSeverity(),
                issueEntity.getType()
        );
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    private Issue getIssue(final int id, final String category, final int columnEnd, final int columnStart,
            final String description, final String fileName, final String fingerprint, final int lineEnd,
            final int lineStart, final String message, final String moduleName, final String origin, final String originName,
            final String packageName, final String reference, final String severity, final String type) {
        IssueBuilder issueBuilder = new IssueBuilder();

        return issueBuilder
                .setCategory(category)
                .setColumnEnd(columnEnd)
                .setColumnStart(columnStart)
                .setDescription(description)
                .setFileName(fileName)
                .setFingerprint(fingerprint)
                .setLineEnd(lineEnd)
                .setLineStart(lineStart)
                .setMessage(message)
                .setModuleName(moduleName)
                .setOrigin(origin)
                .setOriginName(originName)
                .setPackageName(packageName)
                .setReference(reference)
                .setSeverity(Severity.valueOf(severity))
                .setType(type)
                .build();
    }
}
