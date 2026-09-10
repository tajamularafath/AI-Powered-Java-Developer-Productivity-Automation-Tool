package com.aiproductivity.java_dev_tool.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CodeAuditResponse {
    private Long reportId;
    private String targetClassName;
    private int overallScore;
    private int totalIssues;
    private int criticalCount;
    private int highCount;
    private int mediumCount;
    private int lowCount;
    private int infoCount;
    private String executiveSummary;
    private String remediatedCode;
    private LocalDateTime createdAt;
    private List<CodeAuditIssueDto> issues = new ArrayList<>();

    public CodeAuditResponse() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public int getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(int totalIssues) {
        this.totalIssues = totalIssues;
    }

    public int getCriticalCount() {
        return criticalCount;
    }

    public void setCriticalCount(int criticalCount) {
        this.criticalCount = criticalCount;
    }

    public int getHighCount() {
        return highCount;
    }

    public void setHighCount(int highCount) {
        this.highCount = highCount;
    }

    public int getMediumCount() {
        return mediumCount;
    }

    public void setMediumCount(int mediumCount) {
        this.mediumCount = mediumCount;
    }

    public int getLowCount() {
        return lowCount;
    }

    public void setLowCount(int lowCount) {
        this.lowCount = lowCount;
    }

    public int getInfoCount() {
        return infoCount;
    }

    public void setInfoCount(int infoCount) {
        this.infoCount = infoCount;
    }

    public String getExecutiveSummary() {
        return executiveSummary;
    }

    public void setExecutiveSummary(String executiveSummary) {
        this.executiveSummary = executiveSummary;
    }

    public String getRemediatedCode() {
        return remediatedCode;
    }

    public void setRemediatedCode(String remediatedCode) {
        this.remediatedCode = remediatedCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CodeAuditIssueDto> getIssues() {
        return issues;
    }

    public void setIssues(List<CodeAuditIssueDto> issues) {
        this.issues = issues;
    }
}
