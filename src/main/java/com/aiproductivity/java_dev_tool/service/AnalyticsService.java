package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.response.DashboardMetricsResponse;
import com.aiproductivity.java_dev_tool.dto.response.WorkflowResponse;
import com.aiproductivity.java_dev_tool.entity.AutomationWorkflow;
import com.aiproductivity.java_dev_tool.repository.AutomationWorkflowRepository;
import com.aiproductivity.java_dev_tool.repository.CodeAuditReportRepository;
import com.aiproductivity.java_dev_tool.repository.ProductivityMetricRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final AutomationWorkflowRepository workflowRepository;
    private final ProductivityMetricRepository metricRepository;
    private final CodeAuditReportRepository auditReportRepository;

    public AnalyticsService(AutomationWorkflowRepository workflowRepository,
                            ProductivityMetricRepository metricRepository,
                            CodeAuditReportRepository auditReportRepository) {
        this.workflowRepository = workflowRepository;
        this.metricRepository = metricRepository;
        this.auditReportRepository = auditReportRepository;
    }

    public DashboardMetricsResponse getDashboardMetrics() {
        DashboardMetricsResponse response = new DashboardMetricsResponse();

        Double totalMinutes = metricRepository.getTotalMinutesSaved();
        response.setTotalHoursSaved(totalMinutes != null ? Math.round((totalMinutes / 60.0) * 10.0) / 10.0 : 0.0);

        long totalWorkflows = workflowRepository.count();
        response.setTotalWorkflowsExecuted(totalWorkflows);

        Long completedWorkflows = workflowRepository.countCompletedWorkflows();
        long completed = completedWorkflows != null ? completedWorkflows : 0L;
        response.setTotalCompletedWorkflows(completed);

        double successRate = totalWorkflows > 0 ? Math.round(((double) completed / totalWorkflows) * 1000.0) / 10.0 : 100.0;
        response.setWorkflowSuccessRate(successRate);

        Long loc = metricRepository.getTotalLinesOfCodeGenerated();
        response.setTotalLinesOfCodeGenerated(loc != null ? loc : 0L);

        Long tests = metricRepository.getTotalTestsSynthesized();
        response.setTotalTestsSynthesized(tests != null ? tests : 0L);

        Long vuln = metricRepository.getTotalVulnerabilitiesPrevented();
        response.setTotalVulnerabilitiesPrevented(vuln != null ? vuln : 0L);

        Double avgScore = auditReportRepository.getAverageAuditScore();
        response.setAverageAuditScore(avgScore != null ? Math.round(avgScore * 10.0) / 10.0 : 95.0);

        // Fetch top 5 recent workflows
        List<AutomationWorkflow> recent = workflowRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, 5)).getContent();
        List<WorkflowResponse> recentDtos = new ArrayList<>();
        for (AutomationWorkflow w : recent) {
            WorkflowResponse dto = new WorkflowResponse();
            dto.setId(w.getId());
            dto.setWorkflowName(w.getWorkflowName());
            dto.setWorkflowType(w.getWorkflowType());
            dto.setStatus(w.getStatus());
            dto.setTargetEntityName(w.getTargetEntityName());
            dto.setOutputSummary(w.getOutputSummary());
            dto.setTotalExecutionTimeMs(w.getTotalExecutionTimeMs());
            dto.setEstimatedMinutesSaved(w.getEstimatedMinutesSaved());
            dto.setCreatedAt(w.getCreatedAt());
            dto.setCompletedAt(w.getCompletedAt());
            recentDtos.add(dto);
        }
        response.setRecentWorkflows(recentDtos);

        return response;
    }
}
