package com.aiproductivity.java_dev_tool.dto.response;

import com.aiproductivity.java_dev_tool.entity.IssueCategory;
import com.aiproductivity.java_dev_tool.entity.IssueSeverity;

public class CodeAuditIssueDto {
    private Long id;
    private String ruleId;
    private String ruleName;
    private IssueSeverity severity;
    private IssueCategory category;
    private Integer lineNumber;
    private String lineSnippet;
    private String issueDescription;
    private String remediationGuidance;
    private String suggestedCodeReplacement;

    public CodeAuditIssueDto() {
    }

    public CodeAuditIssueDto(String ruleId, String ruleName, IssueSeverity severity, IssueCategory category,
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
