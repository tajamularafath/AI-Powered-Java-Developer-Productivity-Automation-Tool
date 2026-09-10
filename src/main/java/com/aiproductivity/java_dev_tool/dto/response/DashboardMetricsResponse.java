package com.aiproductivity.java_dev_tool.dto.response;

import java.util.ArrayList;
import java.util.List;

public class DashboardMetricsResponse {
    private double totalHoursSaved;
    private long totalWorkflowsExecuted;
    private long totalCompletedWorkflows;
    private long totalLinesOfCodeGenerated;
    private long totalTestsSynthesized;
    private long totalVulnerabilitiesPrevented;
    private double averageAuditScore;
    private double workflowSuccessRate;
    private List<WorkflowResponse> recentWorkflows = new ArrayList<>();

    public DashboardMetricsResponse() {
    }

    // Getters and Setters

    public double getTotalHoursSaved() {
        return totalHoursSaved;
    }

    public void setTotalHoursSaved(double totalHoursSaved) {
        this.totalHoursSaved = totalHoursSaved;
    }

    public long getTotalWorkflowsExecuted() {
        return totalWorkflowsExecuted;
    }

    public void setTotalWorkflowsExecuted(long totalWorkflowsExecuted) {
        this.totalWorkflowsExecuted = totalWorkflowsExecuted;
    }

    public long getTotalCompletedWorkflows() {
        return totalCompletedWorkflows;
    }

    public void setTotalCompletedWorkflows(long totalCompletedWorkflows) {
        this.totalCompletedWorkflows = totalCompletedWorkflows;
    }

    public long getTotalLinesOfCodeGenerated() {
        return totalLinesOfCodeGenerated;
    }

    public void setTotalLinesOfCodeGenerated(long totalLinesOfCodeGenerated) {
        this.totalLinesOfCodeGenerated = totalLinesOfCodeGenerated;
    }

    public long getTotalTestsSynthesized() {
        return totalTestsSynthesized;
    }

    public void setTotalTestsSynthesized(long totalTestsSynthesized) {
        this.totalTestsSynthesized = totalTestsSynthesized;
    }

    public long getTotalVulnerabilitiesPrevented() {
        return totalVulnerabilitiesPrevented;
    }

    public void setTotalVulnerabilitiesPrevented(long totalVulnerabilitiesPrevented) {
        this.totalVulnerabilitiesPrevented = totalVulnerabilitiesPrevented;
    }

    public double getAverageAuditScore() {
        return averageAuditScore;
    }

    public void setAverageAuditScore(double averageAuditScore) {
        this.averageAuditScore = averageAuditScore;
    }

    public double getWorkflowSuccessRate() {
        return workflowSuccessRate;
    }

    public void setWorkflowSuccessRate(double workflowSuccessRate) {
        this.workflowSuccessRate = workflowSuccessRate;
    }

    public List<WorkflowResponse> getRecentWorkflows() {
        return recentWorkflows;
    }

    public void setRecentWorkflows(List<WorkflowResponse> recentWorkflows) {
        this.recentWorkflows = recentWorkflows;
    }
}
