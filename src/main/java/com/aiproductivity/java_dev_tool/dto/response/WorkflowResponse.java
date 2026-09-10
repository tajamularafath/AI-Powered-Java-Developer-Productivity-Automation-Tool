package com.aiproductivity.java_dev_tool.dto.response;

import com.aiproductivity.java_dev_tool.entity.WorkflowStatus;
import com.aiproductivity.java_dev_tool.entity.WorkflowType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkflowResponse {
    private Long id;
    private String workflowName;
    private WorkflowType workflowType;
    private WorkflowStatus status;
    private String targetEntityName;
    private String targetPackage;
    private String outputSummary;
    private Long totalExecutionTimeMs;
    private Double estimatedMinutesSaved;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private List<WorkflowStepDto> steps = new ArrayList<>();
    private List<GeneratedArtifactDto> artifacts = new ArrayList<>();

    public WorkflowResponse() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public WorkflowType getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(WorkflowType workflowType) {
        this.workflowType = workflowType;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }

    public String getTargetEntityName() {
        return targetEntityName;
    }

    public void setTargetEntityName(String targetEntityName) {
        this.targetEntityName = targetEntityName;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getOutputSummary() {
        return outputSummary;
    }

    public void setOutputSummary(String outputSummary) {
        this.outputSummary = outputSummary;
    }

    public Long getTotalExecutionTimeMs() {
        return totalExecutionTimeMs;
    }

    public void setTotalExecutionTimeMs(Long totalExecutionTimeMs) {
        this.totalExecutionTimeMs = totalExecutionTimeMs;
    }

    public Double getEstimatedMinutesSaved() {
        return estimatedMinutesSaved;
    }

    public void setEstimatedMinutesSaved(Double estimatedMinutesSaved) {
        this.estimatedMinutesSaved = estimatedMinutesSaved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public List<WorkflowStepDto> getSteps() {
        return steps;
    }

    public void setSteps(List<WorkflowStepDto> steps) {
        this.steps = steps;
    }

    public List<GeneratedArtifactDto> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<GeneratedArtifactDto> artifacts) {
        this.artifacts = artifacts;
    }
}
