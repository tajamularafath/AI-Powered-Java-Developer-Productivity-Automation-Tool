package com.aiproductivity.java_dev_tool.controller;

import com.aiproductivity.java_dev_tool.dto.request.DirectCodeGenRequest;
import com.aiproductivity.java_dev_tool.dto.response.ApiResponse;
import com.aiproductivity.java_dev_tool.dto.response.GeneratedArtifactDto;
import com.aiproductivity.java_dev_tool.service.CodeGenerationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/codegen")
@CrossOrigin(origins = "*")
public class CodeGenController {

    private final CodeGenerationService codeGenerationService;

    public CodeGenController(CodeGenerationService codeGenerationService) {
        this.codeGenerationService = codeGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GeneratedArtifactDto>> generateArtifact(@Valid @RequestBody DirectCodeGenRequest request) {
        GeneratedArtifactDto artifact = codeGenerationService.generateArtifact(request);
        return ResponseEntity.ok(ApiResponse.success("Artifact generated successfully", artifact));
    }
}
