package com.aiproductivity.java_dev_tool.controller;

import com.aiproductivity.java_dev_tool.dto.request.TestGenerationRequest;
import com.aiproductivity.java_dev_tool.dto.response.ApiResponse;
import com.aiproductivity.java_dev_tool.dto.response.TestGenerationResponse;
import com.aiproductivity.java_dev_tool.service.TestSynthesisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tests")
@CrossOrigin(origins = "*")
public class TestSynthesisController {

    private final TestSynthesisService testSynthesisService;

    public TestSynthesisController(TestSynthesisService testSynthesisService) {
        this.testSynthesisService = testSynthesisService;
    }

    @PostMapping("/synthesize")
    public ResponseEntity<ApiResponse<TestGenerationResponse>> synthesizeTests(@Valid @RequestBody TestGenerationRequest request) {
        TestGenerationResponse response = testSynthesisService.synthesizeTests(request);
        return ResponseEntity.ok(ApiResponse.success("Unit test suite synthesized successfully", response));
    }
}
