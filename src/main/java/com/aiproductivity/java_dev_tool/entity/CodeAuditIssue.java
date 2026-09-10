package com.aiproductivity.java_dev_tool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "code_audit_issues")
public class CodeAuditIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @JsonIgnore
    private CodeAuditReport report;

    @Column(name = "rule_id", nullable = false)
    private String ruleId;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private IssueSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private IssueCategory category;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Column(name = "line_snippet")
    private String lineSnippet;

    @Lob
    @Column(name = "issue_description", columnDefinition = "TEXT")
    private String issueDescription;

    @Lob
    @Column(name = "remediation_guidance", columnDefinition = "TEXT")
    private String remediationGuidance;

    @Lob
    @Column(name = "suggested_code_replacement", columnDefinition = "TEXT")
    private String suggestedCodeReplacement;

    public CodeAuditIssue() {
    }

    public CodeAuditIssue(String ruleId, String ruleName, IssueSeverity severity, IssueCategory category,
                          Integer lineNumber, String lineSnippet, String issueDescription,
                          String remediationGuidance, String suggestedCodeReplacement) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.severity = severity;
        this.category = category;
        this.lineNumber = lineNumber;
        this.lineSnippet = lineSnippet;
        this.issueDescription = issueDescription;
        this.remediationGuidance = remediationGuidance;
        this.suggestedCodeReplacement = suggestedCodeReplacement;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CodeAuditReport getReport() {
        return report;
    }

    public void setReport(CodeAuditReport report) {
        this.report = report;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public IssueSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(IssueSeverity severity) {
        this.severity = severity;
    }

    public IssueCategory getCategory() {
        return category;
    }

    public void setCategory(IssueCategory category) {
        this.category = category;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineSnippet() {
        return lineSnippet;
    }

    public void setLineSnippet(String lineSnippet) {
        this.lineSnippet = lineSnippet;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getRemediationGuidance() {
        return remediationGuidance;
    }

    public void setRemediationGuidance(String remediationGuidance) {
        this.remediationGuidance = remediationGuidance;
    }

    public String getSuggestedCodeReplacement() {
        return suggestedCodeReplacement;
    }

    public void setSuggestedCodeReplacement(String suggestedCodeReplacement) {
        this.suggestedCodeReplacement = suggestedCodeReplacement;
    }
}
