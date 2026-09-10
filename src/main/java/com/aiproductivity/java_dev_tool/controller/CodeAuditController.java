package com.aiproductivity.java_dev_tool.controller;

import com.aiproductivity.java_dev_tool.dto.request.CodeAuditRequest;
import com.aiproductivity.java_dev_tool.dto.response.ApiResponse;
import com.aiproductivity.java_dev_tool.dto.response.CodeAuditResponse;
import com.aiproductivity.java_dev_tool.entity.CodeAuditReport;
import com.aiproductivity.java_dev_tool.service.CodeAuditService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit")
@CrossOrigin(origins = "*")
public class CodeAuditController {

    private final CodeAuditService codeAuditService;

    public CodeAuditController(CodeAuditService codeAuditService) {
        this.codeAuditService = codeAuditService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CodeAuditResponse>> auditSourceCode(@Valid @RequestBody CodeAuditRequest request) {
        CodeAuditResponse response = codeAuditService.auditAndPersist(request);
        return ResponseEntity.ok(ApiResponse.success("Code audit completed successfully", response));
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<Page<CodeAuditReport>>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CodeAuditReport> reports = codeAuditService.getAllReports(pageable);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<ApiResponse<CodeAuditReport>> getReportById(@PathVariable Long id) {
        CodeAuditReport report = codeAuditService.getReportById(id);
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
