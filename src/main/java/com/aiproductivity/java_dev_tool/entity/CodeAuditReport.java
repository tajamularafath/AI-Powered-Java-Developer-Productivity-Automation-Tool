package com.aiproductivity.java_dev_tool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "code_audit_reports")
public class CodeAuditReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_class_name", nullable = false)
    private String targetClassName;

    @Column(name = "overall_score", nullable = false)
    private Integer overallScore = 100;

    @Column(name = "total_issues", nullable = false)
    private Integer totalIssues = 0;

    @Column(name = "critical_count", nullable = false)
    private Integer criticalCount = 0;

    @Column(name = "high_count", nullable = false)
    private Integer highCount = 0;

    @Column(name = "medium_count", nullable = false)
    private Integer mediumCount = 0;

    @Column(name = "low_count", nullable = false)
    private Integer lowCount = 0;

    @Column(name = "info_count", nullable = false)
    private Integer infoCount = 0;

    @Lob
    @Column(name = "raw_source_code", columnDefinition = "LONGTEXT")
    private String rawSourceCode;

    @Lob
    @Column(name = "executive_summary", columnDefinition = "TEXT")
    private String executiveSummary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CodeAuditIssue> issues = new ArrayList<>();

    public CodeAuditReport() {
    }

    public CodeAuditReport(String targetClassName) {
        this.targetClassName = targetClassName;
        this.createdAt = LocalDateTime.now();
    }

    public void addIssue(CodeAuditIssue issue) {
        issues.add(issue);
        issue.setReport(this);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public Integer getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(Integer totalIssues) {
        this.totalIssues = totalIssues;
    }

    public Integer getCriticalCount() {
        return criticalCount;
    }

    public void setCriticalCount(Integer criticalCount) {
        this.criticalCount = criticalCount;
    }

    public Integer getHighCount() {
        return highCount;
    }

    public void setHighCount(Integer highCount) {
        this.highCount = highCount;
    }

    public Integer getMediumCount() {
        return mediumCount;
    }

    public void setMediumCount(Integer mediumCount) {
        this.mediumCount = mediumCount;
    }

    public Integer getLowCount() {
        return lowCount;
    }

    public void setLowCount(Integer lowCount) {
        this.lowCount = lowCount;
    }

    public Integer getInfoCount() {
        return infoCount;
    }

    public void setInfoCount(Integer infoCount) {
        this.infoCount = infoCount;
    }

    public String getRawSourceCode() {
        return rawSourceCode;
    }

    public void setRawSourceCode(String rawSourceCode) {
        this.rawSourceCode = rawSourceCode;
    }

    public String getExecutiveSummary() {
        return executiveSummary;
    }

    public void setExecutiveSummary(String executiveSummary) {
        this.executiveSummary = executiveSummary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CodeAuditIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<CodeAuditIssue> issues) {
        this.issues = issues;
    }
}
