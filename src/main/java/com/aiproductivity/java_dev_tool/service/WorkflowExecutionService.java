package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.request.CodeAuditRequest;
import com.aiproductivity.java_dev_tool.dto.request.EntityFieldDefinition;
import com.aiproductivity.java_dev_tool.dto.request.ModernizeCodeRequest;
import com.aiproductivity.java_dev_tool.dto.request.ScaffoldWorkflowRequest;
import com.aiproductivity.java_dev_tool.dto.request.TestGenerationRequest;
import com.aiproductivity.java_dev_tool.dto.response.*;
import com.aiproductivity.java_dev_tool.entity.*;
import com.aiproductivity.java_dev_tool.exception.ResourceNotFoundException;
import com.aiproductivity.java_dev_tool.exception.WorkflowExecutionException;
import com.aiproductivity.java_dev_tool.repository.AutomationWorkflowRepository;
import com.aiproductivity.java_dev_tool.repository.ProductivityMetricRepository;
import com.aiproductivity.java_dev_tool.repository.WorkflowStepExecutionRepository;
import com.aiproductivity.java_dev_tool.service.ai.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WorkflowExecutionService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowExecutionService.class);

    private final AutomationWorkflowRepository workflowRepository;
    private final WorkflowStepExecutionRepository stepRepository;
    private final ProductivityMetricRepository metricRepository;
    private final CodeGenerationService codeGenService;
    private final AiService aiService;

    public WorkflowExecutionService(AutomationWorkflowRepository workflowRepository,
                                    WorkflowStepExecutionRepository stepRepository,
                                    ProductivityMetricRepository metricRepository,
                                    CodeGenerationService codeGenService,
                                    @Qualifier("geminiCloudAiService") AiService aiService) {
        this.workflowRepository = workflowRepository;
        this.stepRepository = stepRepository;
        this.metricRepository = metricRepository;
        this.codeGenService = codeGenService;
        this.aiService = aiService;
    }

    public WorkflowResponse executeScaffoldWorkflow(ScaffoldWorkflowRequest request) {
        long startTime = System.currentTimeMillis();
        String entityName = capitalize(request.getEntityName());
        String pkg = request.getBasePackage() != null ? request.getBasePackage() : "com.example.demo";
        List<EntityFieldDefinition> fields = request.getFields() != null && !request.getFields().isEmpty()
                ? request.getFields()
                : defaultFields();

        log.info("Starting Full Scaffolding Automation Workflow for entity: {} in package: {}", entityName, pkg);

        // 1. Initialize Workflow Record
        AutomationWorkflow workflow = new AutomationWorkflow(
                "Autonomous Scaffolding: " + entityName,
                WorkflowType.FULL_SCAFFOLD,
                entityName,
                pkg
        );
        workflow.setStatus(WorkflowStatus.IN_PROGRESS);
        workflow.setInputPayload(String.format("Entity: %s, Fields: %d, Package: %s", entityName, fields.size(), pkg));
        AutomationWorkflow savedWorkflow = workflowRepository.save(workflow);

        List<GeneratedArtifactDto> artifacts = new ArrayList<>();
        int totalLoc = 0;
        int stepOrder = 1;

        try {
            // Step 1: Spec Parsing & Architecture Validation
            long s1Time = System.currentTimeMillis();
            WorkflowStepExecution step1 = new WorkflowStepExecution(
                    "Specification & Architecture Validation",
                    stepOrder++,
                    ArtifactType.SPEC_PARSING,
                    entityName + "Spec"
            );
            step1.setStatus(WorkflowStatus.SUCCESS);
            String specSummary = String.format("Architecture Plan validated for %s: %d attributes, standard REST CRUD, JPA auditing, DDL migration.", entityName, fields.size());
            step1.setGeneratedContent(specSummary);
            step1.setExecutionDurationMs(System.currentTimeMillis() - s1Time);
            savedWorkflow.addStepExecution(step1);

            // Step 2: JPA Entity Generation
            long s2Time = System.currentTimeMillis();
            String entityCode = codeGenService.generateEntityCode(entityName, pkg, fields);
            WorkflowStepExecution step2 = new WorkflowStepExecution("JPA Entity Synthesis", stepOrder++, ArtifactType.ENTITY, entityName + ".java");
            step2.setStatus(WorkflowStatus.SUCCESS);
            step2.setGeneratedContent(entityCode);
            step2.setExecutionDurationMs(System.currentTimeMillis() - s2Time);
            savedWorkflow.addStepExecution(step2);
            artifacts.add(new GeneratedArtifactDto(ArtifactType.ENTITY, entityName + ".java", pkg + ".entity", entityCode));
            totalLoc += countLines(entityCode);

            // Step 3: DTO Generation (Request & Response)
            if (request.isGenerateDto()) {
                long s3Time = System.currentTimeMillis();
                String reqDto = codeGenService.generateRequestDtoCode(entityName, pkg, fields);
                String resDto = codeGenService.generateResponseDtoCode(entityName, pkg, fields);

                WorkflowStepExecution step3a = new WorkflowStepExecution("Request DTO Synthesis", stepOrder++, ArtifactType.REQUEST_DTO, entityName + "RequestDto.java");
                step3a.setStatus(WorkflowStatus.SUCCESS);
                step3a.setGeneratedContent(reqDto);
                step3a.setExecutionDurationMs(System.currentTimeMillis() - s3Time);
                savedWorkflow.addStepExecution(step3a);
                artifacts.add(new GeneratedArtifactDto(ArtifactType.REQUEST_DTO, entityName + "RequestDto.java", pkg + ".dto.request", reqDto));
                totalLoc += countLines(reqDto);

                WorkflowStepExecution step3b = new WorkflowStepExecution("Response DTO Synthesis", stepOrder++, ArtifactType.RESPONSE_DTO, entityName + "ResponseDto.java");
                step3b.setStatus(WorkflowStatus.SUCCESS);
                step3b.setGeneratedContent(resDto);
                step3b.setExecutionDurationMs(System.currentTimeMillis() - s3Time);
                savedWorkflow.addStepExecution(step3b);
                artifacts.add(new GeneratedArtifactDto(ArtifactType.RESPONSE_DTO, entityName + "ResponseDto.java", pkg + ".dto.response", resDto));
                totalLoc += countLines(resDto);
            }

            // Step 4: Spring Data JPA Repository Generation
            long s4Time = System.currentTimeMillis();
            String repoCode = codeGenService.generateRepositoryCode(entityName, pkg, fields);
            WorkflowStepExecution step4 = new WorkflowStepExecution("Repository Synthesis", stepOrder++, ArtifactType.REPOSITORY, entityName + "Repository.java");
            step4.setStatus(WorkflowStatus.SUCCESS);
            step4.setGeneratedContent(repoCode);
            step4.setExecutionDurationMs(System.currentTimeMillis() - s4Time);
            savedWorkflow.addStepExecution(step4);
            artifacts.add(new GeneratedArtifactDto(ArtifactType.REPOSITORY, entityName + "Repository.java", pkg + ".repository", repoCode));
            totalLoc += countLines(repoCode);

            // Step 5: Service Layer Generation
            if (request.isGenerateService()) {
                long s5Time = System.currentTimeMillis();
                String srvCode = codeGenService.generateServiceCode(entityName, pkg, fields);
                String srvImplCode = codeGenService.generateServiceImplCode(entityName, pkg, fields);

                WorkflowStepExecution step5a = new WorkflowStepExecution("Service Interface Synthesis", stepOrder++, ArtifactType.SERVICE, entityName + "Service.java");
                step5a.setStatus(WorkflowStatus.SUCCESS);
                step5a.setGeneratedContent(srvCode);
                step5a.setExecutionDurationMs(System.currentTimeMillis() - s5Time);
                savedWorkflow.addStepExecution(step5a);
                artifacts.add(new GeneratedArtifactDto(ArtifactType.SERVICE, entityName + "Service.java", pkg + ".service", srvCode));
                totalLoc += countLines(srvCode);

                WorkflowStepExecution step5b = new WorkflowStepExecution("Service Implementation Synthesis", stepOrder++, ArtifactType.SERVICE_IMPL, entityName + "ServiceImpl.java");
                step5b.setStatus(WorkflowStatus.SUCCESS);
                step5b.setGeneratedContent(srvImplCode);
                step5b.setExecutionDurationMs(System.currentTimeMillis() - s5Time);
                savedWorkflow.addStepExecution(step5b);
                artifacts.add(new GeneratedArtifactDto(ArtifactType.SERVICE_IMPL, entityName + "ServiceImpl.java", pkg + ".service.impl", srvImplCode));
                totalLoc += countLines(srvImplCode);
            }

            // Step 6: REST Controller Generation
            if (request.isGenerateController()) {
                long s6Time = System.currentTimeMillis();
                String ctrlCode = codeGenService.generateControllerCode(entityName, pkg, fields);
                WorkflowStepExecution step6 = new WorkflowStepExecution("REST Controller Synthesis", stepOrder++, ArtifactType.CONTROLLER, entityName + "Controller.java");
                step6.setStatus(WorkflowStatus.SUCCESS);
                step6.setGeneratedContent(ctrlCode);
                step6.setExecutionDurationMs(System.currentTimeMillis() - s6Time);
                savedWorkflow.addStepExecution(step6);
                artifacts.add(new GeneratedArtifactDto(ArtifactType.CONTROLLER, entityName + "Controller.java", pkg + ".controller", ctrlCode));
                totalLoc += countLines(ctrlCode);
            }

            // Step 7: MySQL DDL Migration Script
            if (request.isGenerateDdl()) {
                long s7Time = System.currentTimeMillis();
                String ddl = codeGenService.generateDdlScript(entityName, fields);
                String ddlName = "V1__create_" + toSnakeCase(entityName) + "_table.sql";
                WorkflowStepExecution step7 = new WorkflowStepExecution("MySQL DDL Migration Generation", stepOrder++, ArtifactType.DDL_MIGRATION, ddlName);
                step7.setStatus(WorkflowStatus.SUCCESS);
                step7.setGeneratedContent(ddl);
                step7.setExecutionDurationMs(System.currentTimeMillis() - s7Time);
                savedWorkflow.addStepExecution(step7);
                artifacts.add(new GeneratedArtifactDto(ArtifactType.DDL_MIGRATION, ddlName, "db.migration", ddl));
                totalLoc += countLines(ddl);
            }

            // Step 8: JUnit 5 + Mockito Test Synthesis
            int testsSynthesized = 0;
            if (request.isGenerateTests()) {
                long s8Time = System.currentTimeMillis();
                TestGenerationResponse testResp = aiService.synthesizeTests(entityName + "ServiceImpl", pkg + ".service.impl", "", "SERVICE");
                WorkflowStepExecution step8 = new WorkflowStepExecution("Automated Unit Test Synthesis", stepOrder++, ArtifactType.TEST_SUITE, testResp.getTestClassName() + ".java");
                step8.setStatus(WorkflowStatus.SUCCESS);
                step8.setGeneratedContent(testResp.getGeneratedTestCode());
                step8.setExecutionDurationMs(System.currentTimeMillis() - s8Time);
                savedWorkflow.addStepExecution(step8);
                artifacts.add(new GeneratedArtifactDto(ArtifactType.TEST_SUITE, testResp.getTestClassName() + ".java", pkg + ".service.impl", testResp.getGeneratedTestCode()));
                totalLoc += countLines(testResp.getGeneratedTestCode());
                testsSynthesized = testResp.getGeneratedTestCasesCount();
            }

            // Step 9: Automated Code Quality & Vulnerability Audit
            if (request.isRunQualityAudit()) {
                long s9Time = System.currentTimeMillis();
                CodeAuditResponse audit = aiService.auditCode(entityName + "ServiceImpl", entityCode);
                WorkflowStepExecution step9 = new WorkflowStepExecution("Quality & Security Audit", stepOrder++, ArtifactType.AUDIT_REPORT, "AuditReport.json");
                step9.setStatus(WorkflowStatus.SUCCESS);
                step9.setGeneratedContent(String.format("Audit Score: %d/100. Issues Found: %d. Executive Summary: %s",
                        audit.getOverallScore(), audit.getTotalIssues(), audit.getExecutiveSummary()));
                step9.setExecutionDurationMs(System.currentTimeMillis() - s9Time);
                savedWorkflow.addStepExecution(step9);
            }

            // Finalize Workflow
            long totalDuration = System.currentTimeMillis() - startTime;
            double estimatedMinutesSaved = 150.0 + (fields.size() * 10.0); // Baseline ~2.5 to 3 hours saved per module

            savedWorkflow.setStatus(WorkflowStatus.COMPLETED);
            savedWorkflow.setTotalExecutionTimeMs(totalDuration);
            savedWorkflow.setEstimatedMinutesSaved(estimatedMinutesSaved);
            savedWorkflow.setOutputSummary(String.format("Successfully scaffolded %d artifacts (%d lines of code, %d tests) in %d ms. Estimated developer time saved: %.1f minutes.",
                    artifacts.size(), totalLoc, testsSynthesized, totalDuration, estimatedMinutesSaved));
            savedWorkflow.setCompletedAt(LocalDateTime.now());

            workflowRepository.save(savedWorkflow);

            // Record Productivity Metric
            metricRepository.save(new ProductivityMetric(
                    "FULL_SCAFFOLD",
                    estimatedMinutesSaved,
                    totalLoc,
                    testsSynthesized,
                    0
            ));

            log.info("Workflow {} COMPLETED successfully in {} ms. Saved ~{} minutes.",
                    savedWorkflow.getId(), totalDuration, estimatedMinutesSaved);

            return mapToResponse(savedWorkflow, artifacts);

        } catch (Exception e) {
            log.error("Scaffold workflow failed: {}", e.getMessage(), e);
            savedWorkflow.setStatus(WorkflowStatus.FAILED);
            savedWorkflow.setOutputSummary("Workflow failed: " + e.getMessage());
            savedWorkflow.setTotalExecutionTimeMs(System.currentTimeMillis() - startTime);
            workflowRepository.save(savedWorkflow);
            throw new WorkflowExecutionException("Scaffold workflow execution encountered an error: " + e.getMessage(), e);
        }
    }

    public WorkflowResponse executeModernizeWorkflow(ModernizeCodeRequest request) {
        long startTime = System.currentTimeMillis();
        String modernized = aiService.modernizeCode(request.getSourceCode(), request.getTargetJavaVersion());

        AutomationWorkflow workflow = new AutomationWorkflow(
                "Code Modernization to Java " + request.getTargetJavaVersion(),
                WorkflowType.MODERNIZE,
                "ModernizedSource",
                "com.example.modernized"
        );
        workflow.setStatus(WorkflowStatus.COMPLETED);
        workflow.setTotalExecutionTimeMs(System.currentTimeMillis() - startTime);
        workflow.setEstimatedMinutesSaved(45.0);
        workflow.setOutputSummary("Code modernized successfully to Java " + request.getTargetJavaVersion() + " standards.");
        workflow.setCompletedAt(LocalDateTime.now());

        WorkflowStepExecution step = new WorkflowStepExecution(
                "AST & Pattern Modernization",
                1,
                ArtifactType.MODERNIZED_CODE,
                "ModernizedCode.java"
        );
        step.setStatus(WorkflowStatus.SUCCESS);
        step.setGeneratedContent(modernized);
        step.setExecutionDurationMs(System.currentTimeMillis() - startTime);
        workflow.addStepExecution(step);

        AutomationWorkflow saved = workflowRepository.save(workflow);

        metricRepository.save(new ProductivityMetric(
                "MODERNIZE",
                45.0,
                modernized.split("\n").length,
                0,
                0
        ));

        List<GeneratedArtifactDto> artifacts = List.of(new GeneratedArtifactDto(
                ArtifactType.MODERNIZED_CODE,
                "ModernizedCode.java",
                "com.example.modernized",
                modernized
        ));

        return mapToResponse(saved, artifacts);
    }

    @Transactional(readOnly = true)
    public WorkflowResponse getWorkflowById(Long id) {
        AutomationWorkflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow not found with id: " + id));

        List<GeneratedArtifactDto> artifacts = new ArrayList<>();
        for (WorkflowStepExecution step : workflow.getStepExecutions()) {
            if (step.getGeneratedContent() != null && !step.getGeneratedContent().isBlank()) {
                artifacts.add(new GeneratedArtifactDto(
                        step.getArtifactType(),
                        step.getArtifactName() != null ? step.getArtifactName() : step.getStepName(),
                        workflow.getTargetPackage(),
                        step.getGeneratedContent()
                ));
            }
        }
        return mapToResponse(workflow, artifacts);
    }

    @Transactional(readOnly = true)
    public Page<WorkflowResponse> getAllWorkflows(Pageable pageable) {
        return workflowRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(w -> mapToResponse(w, new ArrayList<>()));
    }

    private WorkflowResponse mapToResponse(AutomationWorkflow w, List<GeneratedArtifactDto> artifacts) {
        WorkflowResponse resp = new WorkflowResponse();
        resp.setId(w.getId());
        resp.setWorkflowName(w.getWorkflowName());
        resp.setWorkflowType(w.getWorkflowType());
        resp.setStatus(w.getStatus());
        resp.setTargetEntityName(w.getTargetEntityName());
        resp.setTargetPackage(w.getTargetPackage());
        resp.setOutputSummary(w.getOutputSummary());
        resp.setTotalExecutionTimeMs(w.getTotalExecutionTimeMs());
        resp.setEstimatedMinutesSaved(w.getEstimatedMinutesSaved());
        resp.setCreatedAt(w.getCreatedAt());
        resp.setCompletedAt(w.getCompletedAt());
        resp.setArtifacts(artifacts);

        List<WorkflowStepDto> stepDtos = new ArrayList<>();
        if (w.getStepExecutions() != null) {
            for (WorkflowStepExecution step : w.getStepExecutions()) {
                WorkflowStepDto sDto = new WorkflowStepDto();
                sDto.setId(step.getId());
                sDto.setStepName(step.getStepName());
                sDto.setStepOrder(step.getStepOrder());
                sDto.setArtifactType(step.getArtifactType());
                sDto.setArtifactName(step.getArtifactName());
                sDto.setStatus(step.getStatus());
                sDto.setGeneratedContent(step.getGeneratedContent());
                sDto.setExecutionDurationMs(step.getExecutionDurationMs());
                sDto.setErrorMessage(step.getErrorMessage());
                sDto.setExecutedAt(step.getExecutedAt());
                stepDtos.add(sDto);
            }
        }
        resp.setSteps(stepDtos);
        return resp;
    }

    private int countLines(String str) {
        if (str == null) return 0;
        return str.split("\r\n|\r|\n").length;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private String toSnakeCase(String str) {
        if (str == null) return "";
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    private List<EntityFieldDefinition> defaultFields() {
        List<EntityFieldDefinition> list = new ArrayList<>();
        list.add(new EntityFieldDefinition("title", "String", false, false, "@NotBlank", "Title of item", "Sample Title"));
        list.add(new EntityFieldDefinition("description", "String", false, true, "@Size(max=1000)", "Detailed description", "Sample description"));
        list.add(new EntityFieldDefinition("price", "BigDecimal", false, false, "@PositiveOrZero", "Item pricing", "99.99"));
        list.add(new EntityFieldDefinition("status", "String", false, false, "@NotBlank", "Item lifecycle state", "ACTIVE"));
        return list;
    }
}
