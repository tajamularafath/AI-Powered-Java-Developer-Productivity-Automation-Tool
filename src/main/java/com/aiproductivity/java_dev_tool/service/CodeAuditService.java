package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.request.CodeAuditRequest;
import com.aiproductivity.java_dev_tool.dto.response.CodeAuditIssueDto;
import com.aiproductivity.java_dev_tool.dto.response.CodeAuditResponse;
import com.aiproductivity.java_dev_tool.entity.CodeAuditIssue;
import com.aiproductivity.java_dev_tool.entity.CodeAuditReport;
import com.aiproductivity.java_dev_tool.entity.ProductivityMetric;
import com.aiproductivity.java_dev_tool.exception.ResourceNotFoundException;
import com.aiproductivity.java_dev_tool.repository.CodeAuditIssueRepository;
import com.aiproductivity.java_dev_tool.repository.CodeAuditReportRepository;
import com.aiproductivity.java_dev_tool.repository.ProductivityMetricRepository;
import com.aiproductivity.java_dev_tool.service.ai.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CodeAuditService {

    private static final Logger log = LoggerFactory.getLogger(CodeAuditService.class);

    private final AiService aiService;
    private final CodeAuditReportRepository reportRepository;
    private final CodeAuditIssueRepository issueRepository;
    private final ProductivityMetricRepository metricRepository;

    public CodeAuditService(@Qualifier("geminiCloudAiService") AiService aiService,
                            CodeAuditReportRepository reportRepository,
                            CodeAuditIssueRepository issueRepository,
                            ProductivityMetricRepository metricRepository) {
        this.aiService = aiService;
        this.reportRepository = reportRepository;
        this.issueRepository = issueRepository;
        this.metricRepository = metricRepository;
    }

    public CodeAuditResponse auditAndPersist(CodeAuditRequest request) {
        log.info("Initiating Code Audit for class: {}", request.getClassName());
        CodeAuditResponse auditResult = aiService.auditCode(request.getClassName(), request.getSourceCode());

        // Persist Report to MySQL
        CodeAuditReport report = new CodeAuditReport(auditResult.getTargetClassName());
        report.setOverallScore(auditResult.getOverallScore());
        report.setTotalIssues(auditResult.getTotalIssues());
        report.setCriticalCount(auditResult.getCriticalCount());
        report.setHighCount(auditResult.getHighCount());
        report.setMediumCount(auditResult.getMediumCount());
        report.setLowCount(auditResult.getLowCount());
        report.setInfoCount(auditResult.getInfoCount());
        report.setRawSourceCode(request.getSourceCode());
        report.setExecutiveSummary(auditResult.getExecutiveSummary());
        report.setCreatedAt(LocalDateTime.now());

        for (CodeAuditIssueDto issueDto : auditResult.getIssues()) {
            CodeAuditIssue issue = new CodeAuditIssue(
                    issueDto.getRuleId(),
                    issueDto.getRuleName(),
                    issueDto.getSeverity(),
                    issueDto.getCategory(),
                    issueDto.getLineNumber(),
                    issueDto.getLineSnippet(),
                    issueDto.getIssueDescription(),
                    issueDto.getRemediationGuidance(),
                    issueDto.getSuggestedCodeReplacement()
            );
            report.addIssue(issue);
        }

        CodeAuditReport savedReport = reportRepository.save(report);
        auditResult.setReportId(savedReport.getId());

        // Record Productivity Metric
        double timeSavedMinutes = 25.0 + (auditResult.getTotalIssues() * 10.0);
        metricRepository.save(new ProductivityMetric(
                "CODE_AUDIT",
                timeSavedMinutes,
                request.getSourceCode() != null ? request.getSourceCode().split("\n").length : 0,
                0,
                auditResult.getCriticalCount() + auditResult.getHighCount()
        ));

        log.info("Code Audit completed with score: {}/100, persisted as Report ID: {}", auditResult.getOverallScore(), savedReport.getId());
        return auditResult;
    }

    @Transactional(readOnly = true)
    public CodeAuditReport getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Code audit report not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<CodeAuditReport> getAllReports(Pageable pageable) {
        return reportRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
