package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.request.TestGenerationRequest;
import com.aiproductivity.java_dev_tool.dto.response.TestGenerationResponse;
import com.aiproductivity.java_dev_tool.service.ai.AiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TestSynthesisService {

    private final AiService aiService;

    public TestSynthesisService(@Qualifier("geminiCloudAiService") AiService aiService) {
        this.aiService = aiService;
    }

    public TestGenerationResponse synthesizeTests(TestGenerationRequest request) {
        return aiService.synthesizeTests(
                request.getTargetClassName(),
                request.getTargetPackage(),
                request.getSourceCode(),
                request.getTargetLayer()
        );
    }
}
