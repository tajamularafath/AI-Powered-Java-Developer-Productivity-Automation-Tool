package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.request.EntityFieldDefinition;
import com.aiproductivity.java_dev_tool.dto.request.ScaffoldWorkflowRequest;
import com.aiproductivity.java_dev_tool.dto.response.WorkflowResponse;
import com.aiproductivity.java_dev_tool.entity.AutomationWorkflow;
import com.aiproductivity.java_dev_tool.entity.WorkflowStatus;
import com.aiproductivity.java_dev_tool.repository.AutomationWorkflowRepository;
import com.aiproductivity.java_dev_tool.repository.ProductivityMetricRepository;
import com.aiproductivity.java_dev_tool.repository.WorkflowStepExecutionRepository;
import com.aiproductivity.java_dev_tool.service.ai.GeminiCloudAiService;
import com.aiproductivity.java_dev_tool.service.ai.LocalHeuristicAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowExecutionServiceTest {

    @Mock
    private AutomationWorkflowRepository workflowRepository;

    @Mock
    private WorkflowStepExecutionRepository stepRepository;

    @Mock
    private ProductivityMetricRepository metricRepository;

    private WorkflowExecutionService workflowExecutionService;

    @BeforeEach
    void setUp() {
        CodeGenerationService codeGenService = new CodeGenerationService();
        LocalHeuristicAiService localAi = new LocalHeuristicAiService();
        GeminiCloudAiService geminiAi = new GeminiCloudAiService(localAi);
        workflowExecutionService = new WorkflowExecutionService(
                workflowRepository,
                stepRepository,
                metricRepository,
                codeGenService,
                geminiAi
        );
    }

    @Test
    @DisplayName("Should orchestrate full multi-step scaffolding pipeline successfully")
    void testExecuteScaffoldWorkflow() {
        ScaffoldWorkflowRequest request = new ScaffoldWorkflowRequest(
                "Customer",
                "com.example.crm",
                List.of(
                        new EntityFieldDefinition("firstName", "String", false, false),
                        new EntityFieldDefinition("lastName", "String", false, false),
                        new EntityFieldDefinition("email", "String", false, false, "@Email", "Unique user email", "john@example.com")
                )
        );

        when(workflowRepository.save(any(AutomationWorkflow.class))).thenAnswer(inv -> {
            AutomationWorkflow w = inv.getArgument(0);
            w.setId(1L);
            return w;
        });

        WorkflowResponse response = workflowExecutionService.executeScaffoldWorkflow(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(WorkflowStatus.COMPLETED);
        assertThat(response.getTargetEntityName()).isEqualTo("Customer");
        assertThat(response.getArtifacts()).isNotEmpty();
        assertThat(response.getSteps()).isNotEmpty();
        assertThat(response.getEstimatedMinutesSaved()).isGreaterThan(100.0);

        // Verify artifacts include Entity, DTOs, Repository, Service, Controller, Test, DDL
        boolean hasEntity = response.getArtifacts().stream().anyMatch(a -> a.getFileName().equals("Customer.java"));
        boolean hasRepo = response.getArtifacts().stream().anyMatch(a -> a.getFileName().equals("CustomerRepository.java"));
        boolean hasController = response.getArtifacts().stream().anyMatch(a -> a.getFileName().equals("CustomerController.java"));
        boolean hasDdl = response.getArtifacts().stream().anyMatch(a -> a.getFileName().endsWith(".sql"));

        assertThat(hasEntity).isTrue();
        assertThat(hasRepo).isTrue();
        assertThat(hasController).isTrue();
        assertThat(hasDdl).isTrue();

        verify(workflowRepository, atLeastOnce()).save(any(AutomationWorkflow.class));
        verify(metricRepository, times(1)).save(any());
    }
}
