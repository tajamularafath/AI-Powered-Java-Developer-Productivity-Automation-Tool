package com.aiproductivity.java_dev_tool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "productivity_metrics")
public class ProductivityMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_type", nullable = false)
    private String workflowType;

    @Column(name = "minutes_saved", nullable = false)
    private Double minutesSaved = 0.0;

    @Column(name = "lines_of_code_generated", nullable = false)
    private Integer linesOfCodeGenerated = 0;

    @Column(name = "tests_synthesized", nullable = false)
    private Integer testsSynthesized = 0;

    @Column(name = "vulnerabilities_prevented", nullable = false)
    private Integer vulnerabilitiesPrevented = 0;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    public ProductivityMetric() {
    }

    public ProductivityMetric(String workflowType, Double minutesSaved, Integer linesOfCodeGenerated,
                              Integer testsSynthesized, Integer vulnerabilitiesPrevented) {
        this.workflowType = workflowType;
        this.minutesSaved = minutesSaved;
        this.linesOfCodeGenerated = linesOfCodeGenerated;
        this.testsSynthesized = testsSynthesized;
        this.vulnerabilitiesPrevented = vulnerabilitiesPrevented;
        this.recordedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }

    public Double getMinutesSaved() {
        return minutesSaved;
    }

    public void setMinutesSaved(Double minutesSaved) {
        this.minutesSaved = minutesSaved;
    }

    public Integer getLinesOfCodeGenerated() {
        return linesOfCodeGenerated;
    }

    public void setLinesOfCodeGenerated(Integer linesOfCodeGenerated) {
        this.linesOfCodeGenerated = linesOfCodeGenerated;
    }

    public Integer getTestsSynthesized() {
        return testsSynthesized;
    }

    public void setTestsSynthesized(Integer testsSynthesized) {
        this.testsSynthesized = testsSynthesized;
    }

    public Integer getVulnerabilitiesPrevented() {
        return vulnerabilitiesPrevented;
    }

    public void setVulnerabilitiesPrevented(Integer vulnerabilitiesPrevented) {
        this.vulnerabilitiesPrevented = vulnerabilitiesPrevented;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
