package com.aiproductivity.java_dev_tool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "automation_workflows")
public class AutomationWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workflow_name", nullable = false)
    private String workflowName;

    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_type", nullable = false)
    private WorkflowType workflowType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WorkflowStatus status = WorkflowStatus.PENDING;

    @Column(name = "target_entity_name")
    private String targetEntityName;

    @Column(name = "target_package")
    private String targetPackage;

    @Lob
    @Column(name = "input_payload", columnDefinition = "LONGTEXT")
    private String inputPayload;

    @Lob
    @Column(name = "output_summary", columnDefinition = "TEXT")
    private String outputSummary;

    @Column(name = "total_execution_time_ms")
    private Long totalExecutionTimeMs = 0L;

    @Column(name = "estimated_minutes_saved")
    private Double estimatedMinutesSaved = 0.0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WorkflowStepExecution> stepExecutions = new ArrayList<>();

    public AutomationWorkflow() {
    }

    public AutomationWorkflow(String workflowName, WorkflowType workflowType, String targetEntityName, String targetPackage) {
        this.workflowName = workflowName;
        this.workflowType = workflowType;
        this.targetEntityName = targetEntityName;
        this.targetPackage = targetPackage;
        this.status = WorkflowStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void addStepExecution(WorkflowStepExecution step) {
        stepExecutions.add(step);
        step.setWorkflow(this);
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

    public String getInputPayload() {
        return inputPayload;
    }

    public void setInputPayload(String inputPayload) {
        this.inputPayload = inputPayload;
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

    public List<WorkflowStepExecution> getStepExecutions() {
        return stepExecutions;
    }

    public void setStepExecutions(List<WorkflowStepExecution> stepExecutions) {
        this.stepExecutions = stepExecutions;
    }
}
