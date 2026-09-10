package com.aiproductivity.java_dev_tool.config;

import com.aiproductivity.java_dev_tool.entity.*;
import com.aiproductivity.java_dev_tool.repository.AutomationWorkflowRepository;
import com.aiproductivity.java_dev_tool.repository.CodeAuditReportRepository;
import com.aiproductivity.java_dev_tool.repository.ProductivityMetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AutomationWorkflowRepository workflowRepository;
    private final ProductivityMetricRepository metricRepository;
    private final CodeAuditReportRepository reportRepository;

    public DataInitializer(AutomationWorkflowRepository workflowRepository,
                           ProductivityMetricRepository metricRepository,
                           CodeAuditReportRepository reportRepository) {
        this.workflowRepository = workflowRepository;
        this.metricRepository = metricRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    public void run(String... args) {
        try {
            if (workflowRepository.count() > 0) {
                return;
            }

            log.info("Seeding initial developer productivity metrics and workflow templates...");

            // 1. Seed Initial Scaffolding Workflow
            AutomationWorkflow orderWorkflow = new AutomationWorkflow(
                    "Autonomous Scaffolding: Order",
                    WorkflowType.FULL_SCAFFOLD,
                    "Order",
                    "com.enterprise.ecommerce"
            );
            orderWorkflow.setStatus(WorkflowStatus.COMPLETED);
            orderWorkflow.setInputPayload("Entity: Order, Fields: customerId, orderTotal, status, shippingAddress");
            orderWorkflow.setOutputSummary("Successfully scaffolded 7 artifacts (342 lines of code, 5 tests) in 128 ms. Estimated developer time saved: 180.0 minutes.");
            orderWorkflow.setTotalExecutionTimeMs(128L);
            orderWorkflow.setEstimatedMinutesSaved(180.0);
            orderWorkflow.setCompletedAt(LocalDateTime.now().minusHours(2));

            WorkflowStepExecution s1 = new WorkflowStepExecution("Specification & Architecture Validation", 1, ArtifactType.SPEC_PARSING, "OrderSpec");
            s1.setStatus(WorkflowStatus.SUCCESS);
            s1.setExecutionDurationMs(12L);
            s1.setGeneratedContent("Order architecture validated. Configured BigDecimal financial math, JPA audit listeners, and REST endpoints.");
            orderWorkflow.addStepExecution(s1);

            WorkflowStepExecution s2 = new WorkflowStepExecution("JPA Entity Synthesis", 2, ArtifactType.ENTITY, "Order.java");
            s2.setStatus(WorkflowStatus.SUCCESS);
            s2.setExecutionDurationMs(24L);
            s2.setGeneratedContent("// JPA Entity Order with validation and timestamps");
            orderWorkflow.addStepExecution(s2);

            workflowRepository.save(orderWorkflow);

            // 2. Seed Initial Code Audit Report
            CodeAuditReport auditReport = new CodeAuditReport("PaymentProcessingService");
            auditReport.setOverallScore(65);
            auditReport.setTotalIssues(3);
            auditReport.setCriticalCount(1);
            auditReport.setHighCount(1);
            auditReport.setMediumCount(1);
            auditReport.setExecutiveSummary("Found 3 issues: 1 Critical SQL injection risk, 1 High unsafe Optional access, 1 Medium missing @Transactional.");
            auditReport.setRawSourceCode("// Legacy PaymentProcessingService with unparameterized SQL");

            CodeAuditIssue issue1 = new CodeAuditIssue(
                    "SEC-001", "SQL Injection Risk via String Concatenation",
                    IssueSeverity.CRITICAL, IssueCategory.SECURITY, 42,
                    "\"SELECT p FROM Payment p WHERE p.txnId = '\" + txnId + \"'\"",
                    "Dynamic SQL concatenation allows attacker to execute arbitrary SQL commands.",
                    "Use parameterized query with :txnId parameter.",
                    "@Query(\"SELECT p FROM Payment p WHERE p.txnId = :txnId\")\nOptional<Payment> findByTxnId(@Param(\"txnId\") String txnId);"
            );
            auditReport.addIssue(issue1);
            reportRepository.save(auditReport);

            // 3. Seed Productivity Metrics
            metricRepository.save(new ProductivityMetric("FULL_SCAFFOLD", 180.0, 342, 5, 0));
            metricRepository.save(new ProductivityMetric("CODE_AUDIT", 45.0, 180, 0, 2));
            metricRepository.save(new ProductivityMetric("TEST_SYNTHESIS", 60.0, 120, 8, 0));

            log.info("Database initialized with seed productivity metrics.");
        } catch (Exception e) {
            log.warn("Database initialization skipped or deferred: {}", e.getMessage());
        }
    }
}
