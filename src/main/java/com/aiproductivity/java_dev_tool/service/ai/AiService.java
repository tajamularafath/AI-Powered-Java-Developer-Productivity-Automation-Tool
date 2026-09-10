package com.aiproductivity.java_dev_tool.service.ai;

import com.aiproductivity.java_dev_tool.dto.response.CodeAuditResponse;
import com.aiproductivity.java_dev_tool.dto.response.TestGenerationResponse;

public interface AiService {

    String generateContent(String systemInstruction, String userPrompt);

    CodeAuditResponse auditCode(String className, String sourceCode);

    TestGenerationResponse synthesizeTests(String className, String packageName, String sourceCode, String targetLayer);

    String modernizeCode(String sourceCode, int targetJavaVersion);

    boolean isCloudEnabled();
}
