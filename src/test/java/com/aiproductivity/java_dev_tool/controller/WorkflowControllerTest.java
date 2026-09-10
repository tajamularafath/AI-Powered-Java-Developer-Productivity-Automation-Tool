package com.aiproductivity.java_dev_tool.controller;

import com.aiproductivity.java_dev_tool.dto.request.ScaffoldWorkflowRequest;
import com.aiproductivity.java_dev_tool.dto.response.WorkflowResponse;
import com.aiproductivity.java_dev_tool.entity.WorkflowStatus;
import com.aiproductivity.java_dev_tool.entity.WorkflowType;
import com.aiproductivity.java_dev_tool.service.WorkflowExecutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkflowController.class)
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WorkflowExecutionService workflowExecutionService;

    @Test
    @DisplayName("POST /api/v1/workflows/scaffold returns 201 Created with workflow details")
    void testTriggerScaffoldWorkflow() throws Exception {
        ScaffoldWorkflowRequest request = new ScaffoldWorkflowRequest();
        request.setEntityName("Payment");
        request.setBasePackage("com.example.billing");

        WorkflowResponse mockResponse = new WorkflowResponse();
        mockResponse.setId(10L);
        mockResponse.setWorkflowName("Autonomous Scaffolding: Payment");
        mockResponse.setWorkflowType(WorkflowType.FULL_SCAFFOLD);
        mockResponse.setStatus(WorkflowStatus.COMPLETED);
        mockResponse.setTargetEntityName("Payment");
        mockResponse.setTotalExecutionTimeMs(150L);
        mockResponse.setEstimatedMinutesSaved(180.0);
        mockResponse.setArtifacts(new ArrayList<>());
        mockResponse.setSteps(new ArrayList<>());

        when(workflowExecutionService.executeScaffoldWorkflow(any(ScaffoldWorkflowRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/workflows/scaffold")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.targetEntityName").value("Payment"))
                .andExpect(jsonPath("$.data.estimatedMinutesSaved").value(180.0));
    }

    @Test
    @DisplayName("GET /api/v1/workflows/{id} returns 200 OK with workflow")
    void testGetWorkflowById() throws Exception {
        WorkflowResponse mockResponse = new WorkflowResponse();
        mockResponse.setId(10L);
        mockResponse.setWorkflowName("Autonomous Scaffolding: Payment");
        mockResponse.setStatus(WorkflowStatus.COMPLETED);

        when(workflowExecutionService.getWorkflowById(10L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/workflows/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(10));
    }
}
