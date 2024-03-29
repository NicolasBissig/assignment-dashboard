package edu.hm.hafner.java.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * POJO to store a {@link IssueEntity} to the database.
 *
 * @author Deniz Mardin
 */
@Entity
@Table(name = "issues")
@SuppressWarnings({"PMD.GodClass", "PMD.TooManyFields"})
@SuppressFBWarnings("NP")
public class IssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String category;
    private int columnStart;
    private int columnEnd;
    private String description;
    private String fileName;
    private String fingerprint;
    private int lineStart;
    private int lineEnd;
    @Column(length = 1024)
    private String message;
    private String moduleName;
    private String origin;
    private String originName;
    private String packageName;
    private String reference;
    private String severity;
    private String type;
    @ManyToOne
    private ReportEntity issues;

    /**
     * Creates a new instance of {@link IssueEntity}.
     */
    public IssueEntity() {
    }

    /**
     * Creates a new instance of {@link IssueEntity}.
     *
     * @param columnStart
     *         the column start of the {@link IssueEntity}
     * @param columnEnd
     *         the column end of the {@link IssueEntity}
     * @param lineStart
     *         the line start of the {@link IssueEntity}
     * @param lineEnd
     *         the line end of the {@link IssueEntity}
     * @param category
     *         the category of the {@link IssueEntity}
     * @param description
     *         the description of the {@link IssueEntity}
     * @param fileName
     *         the file name of the {@link IssueEntity}
     * @param fingerprint
     *         the fingerprint of the {@link IssueEntity}
     * @param message
     *         the message of the {@link IssueEntity}
     * @param moduleName
     *         the module name of the {@link IssueEntity}
     * @param origin
     *         the origin of the {@link IssueEntity}
     * @param originName
     *         the name of the origin of the {@link IssueEntity}
     * @param packageName
     *         the package name of the {@link IssueEntity}
     * @param reference
     *         the reference of the {@link IssueEntity}
     * @param severity
     *         the severity of the {@link IssueEntity}
     * @param type
     *         the type of the {@link IssueEntity}
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public IssueEntity(
            final int columnStart,
            final int columnEnd,
            final int lineStart,
            final int lineEnd,
            final String category,
            final String description,
            final String fileName,
            final String fingerprint,
            final String message,
            final String moduleName,
            final String origin,
            final String originName,
            final String packageName,
            final String reference,
            final String severity,
            final String type) {
        this.category = category;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
        this.description = description;
        this.fileName = fileName;
        this.fingerprint = fingerprint;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.message = message;
        this.moduleName = moduleName;
        this.origin = origin;
        this.originName = originName;
        this.packageName = packageName;
        this.reference = reference;
        this.severity = severity;
        this.type = type;
    }

    /**
     * Returns the associated {@link ReportEntity} of the {@link IssueEntity}.
     *
     * @return the the {@link ReportEntity}
     */
    public ReportEntity getIssues() {
        return issues;
    }

    /**
     * Setter for the {@link ReportEntity}.
     *
     * @param issues
     *         the {@link ReportEntity}
     */
    public void setIssues(final ReportEntity issues) {
        this.issues = issues;
    }

    /**
     * Returns the UUID of the {@link IssueEntity}.
     *
     * @return the UUID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the UUID.
     *
     * @param id
     *         the UUID
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Returns the category of the {@link IssueEntity}.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Setter for the category of the {@link IssueEntity}.
     *
     * @param category
     *         the category
     */
    public void setCategory(final String category) {
        this.category = category;
    }

    /**
     * Returns the end of the column for the {@link IssueEntity}.
     *
     * @return the end of the column
     */
    public int getColumnEnd() {
        return columnEnd;
    }

    /**
     * Setter to set the end fo the column for the {@link IssueEntity}.
     *
     * @param columnEnd
     *         the end of the column
     */
    public void setColumnEnd(final int columnEnd) {
        this.columnEnd = columnEnd;
    }

    /**
     * Returns the start of the column for the {@link IssueEntity}.
     *
     * @return the start of the column
     */
    public int getColumnStart() {
        return columnStart;
    }

    /**
     * Setter to set the start of the column for the {@link IssueEntity}.
     *
     * @param columnStart
     *         the start of the column
     */
    public void setColumnStart(final int columnStart) {
        this.columnStart = columnStart;
    }

    /**
     * Returns the description for the {@link IssueEntity}.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the {@link IssueEntity}.
     *
     * @param description
     *         the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the file name of the {@link IssueEntity}.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Setter for the file name of the {@link IssueEntity}.
     *
     * @param fileName
     *         the file name
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the fingerprint of the {@link IssueEntity}.
     *
     * @return the fingerprint
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * Setter for the fingerprint of the {@link IssueEntity}.
     *
     * @param fingerprint
     *         the fingerprint
     */
    public void setFingerprint(final String fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * Returns the end of line for the {@link IssueEntity}.
     *
     * @return the end of the line
     */
    public int getLineEnd() {
        return lineEnd;
    }

    /**
     * Setter for the end of the line for the {@link IssueEntity}.
     *
     * @param lineEnd
     *         the end of the line
     */
    public void setLineEnd(final int lineEnd) {
        this.lineEnd = lineEnd;
    }

    /**
     * Returns the start of the line for the {@link IssueEntity}.
     *
     * @return the start of the line
     */
    public int getLineStart() {
        return lineStart;
    }

    /**
     * Setter to set the start of the line for the {@link IssueEntity}.
     *
     * @param lineStart
     *         the start of the line
     */
    public void setLineStart(final int lineStart) {
        this.lineStart = lineStart;
    }

    /**
     * Returns the message of the {@link IssueEntity}.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter to set the message for the {@link IssueEntity}.
     *
     * @param message
     *         the message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Returns the module name of the {@link IssueEntity}.
     *
     * @return the module name
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Setter to set the module name of the {@link IssueEntity}.
     *
     * @param moduleName
     *         the module name
     */
    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Returns the origin of the {@link IssueEntity}.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Setter to set the origin of the {@link IssueEntity}.
     *
     * @param origin
     *         the origin
     */
    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    /**
     * Returns the name of the origin of the {@link IssueEntity}.
     *
     * @return the origin name
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * Setter to set the name of the origin of the {@link IssueEntity}.
     *
     * @param originName
     *         the origin name
     */
    public void setOriginName(final String originName) {
        this.originName = originName;
    }

    /**
     * Returns the package name of the {@link IssueEntity}.
     *
     * @return the package
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Setter to set the package name for the {@link IssueEntity}.
     *
     * @param packageName
     *         the package name
     */
    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    /**
     * Returns the reference of the {@link IssueEntity}.
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Setter to set the reference for the {@link IssueEntity}.
     *
     * @param reference
     *         the reference
     */
    public void setReference(final String reference) {
        this.reference = reference;
    }

    /**
     * Returns the severity of the {@link IssueEntity}.
     *
     * @return the severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Setter to set the severity of the {@link IssueEntity}.
     *
     * @param severity
     *         the severity
     */
    public void setSeverity(final String severity) {
        this.severity = severity;
    }

    /**
     * Returns the type of the {@link IssueEntity}.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter to set the type of the {@link IssueEntity}.
     *
     * @param type
     *         the type
     */
    public void setType(final String type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(@CheckForNull final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueEntity that = (IssueEntity) o;

        if (columnStart != that.columnStart) {
            return false;
        }
        if (columnEnd != that.columnEnd) {
            return false;
        }
        if (lineStart != that.lineStart) {
            return false;
        }
        if (lineEnd != that.lineEnd) {
            return false;
        }
        if (id != that.id) {
            return false;
        }
        if (!category.equals(that.category)) {
            return false;
        }
        if (!description.equals(that.description)) {
            return false;
        }
        if (!fileName.equals(that.fileName)) {
            return false;
        }
        if (!fingerprint.equals(that.fingerprint)) {
            return false;
        }
        if (!message.equals(that.message)) {
            return false;
        }
        if (!moduleName.equals(that.moduleName)) {
            return false;
        }
        if (!origin.equals(that.origin)) {
            return false;
        }
        if (!originName.equals(that.originName)) {
            return false;
        }
        if (!packageName.equals(that.packageName)) {
            return false;
        }
        if (!reference.equals(that.reference)) {
            return false;
        }
        if (!severity.equals(that.severity)) {
            return false;
        }
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + category.hashCode();
        result = 31 * result + columnStart;
        result = 31 * result + columnEnd;
        result = 31 * result + description.hashCode();
        result = 31 * result + fileName.hashCode();
        result = 31 * result + fingerprint.hashCode();
        result = 31 * result + lineStart;
        result = 31 * result + lineEnd;
        result = 31 * result + message.hashCode();
        result = 31 * result + moduleName.hashCode();
        result = 31 * result + origin.hashCode();
        result = 31 * result + originName.hashCode();
        result = 31 * result + packageName.hashCode();
        result = 31 * result + reference.hashCode();
        result = 31 * result + severity.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
