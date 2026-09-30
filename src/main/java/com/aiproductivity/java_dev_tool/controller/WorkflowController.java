package com.aiproductivity.java_dev_tool.controller;

import com.aiproductivity.java_dev_tool.dto.request.ModernizeCodeRequest;
import com.aiproductivity.java_dev_tool.dto.request.ScaffoldWorkflowRequest;
import com.aiproductivity.java_dev_tool.dto.response.ApiResponse;
import com.aiproductivity.java_dev_tool.dto.response.WorkflowResponse;
import com.aiproductivity.java_dev_tool.service.WorkflowExecutionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workflows")
@CrossOrigin(origins = "*")
public class WorkflowController {

    private final WorkflowExecutionService workflowExecutionService;

    public WorkflowController(WorkflowExecutionService workflowExecutionService) {
        this.workflowExecutionService = workflowExecutionService;
    }

    @PostMapping("/scaffold")
    public ResponseEntity<ApiResponse<WorkflowResponse>> triggerScaffoldWorkflow(@Valid @RequestBody ScaffoldWorkflowRequest request) {
        WorkflowResponse response = workflowExecutionService.executeScaffoldWorkflow(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Autonomous scaffolding workflow executed successfully", response));
    }

    @PostMapping("/modernize")
    public ResponseEntity<ApiResponse<WorkflowResponse>> triggerModernizeWorkflow(@Valid @RequestBody ModernizeCodeRequest request) {
        WorkflowResponse response = workflowExecutionService.executeModernizeWorkflow(request);
        return ResponseEntity.ok(ApiResponse.success("Code modernization workflow executed successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkflowResponse>> getWorkflowById(@PathVariable Long id) {
        WorkflowResponse response = workflowExecutionService.getWorkflowById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}/bundle")
    public ResponseEntity<ApiResponse<java.util.Map<String, String>>> getWorkflowBundle(@PathVariable Long id) {
        WorkflowResponse response = workflowExecutionService.getWorkflowById(id);
        java.util.Map<String, String> bundle = new java.util.LinkedHashMap<>();
        if (response.getArtifacts() != null) {
            for (var artifact : response.getArtifacts()) {
                String path = artifact.getPackageName().replace('.', '/') + "/" + artifact.getFileName();
                bundle.put(path, artifact.getContent());
            }
        }
        return ResponseEntity.ok(ApiResponse.success("Bundle retrieved successfully", bundle));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<WorkflowResponse>>> getAllWorkflows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WorkflowResponse> workflows = workflowExecutionService.getAllWorkflows(pageable);
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }
}
