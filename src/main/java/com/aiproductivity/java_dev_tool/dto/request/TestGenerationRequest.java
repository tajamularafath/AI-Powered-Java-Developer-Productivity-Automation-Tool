package com.aiproductivity.java_dev_tool.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class TestGenerationRequest {

    @NotBlank(message = "Target class name is required")
    private String targetClassName;

    private String targetPackage = "com.example.demo.service";

    @NotBlank(message = "Source code or method specification is required")
    private String sourceCode;

    private String targetLayer = "SERVICE"; // SERVICE, CONTROLLER, REPOSITORY

    private boolean mockMvcEnabled = true;

    private boolean generateEdgeCases = true;

    private List<String> methodsToTest = new ArrayList<>();

    public TestGenerationRequest() {
    }

    public TestGenerationRequest(String targetClassName, String targetPackage, String sourceCode) {
        this.targetClassName = targetClassName;
        this.targetPackage = targetPackage;
        this.sourceCode = sourceCode;
    }

    // Getters and Setters

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getTargetLayer() {
        return targetLayer;
    }

    public void setTargetLayer(String targetLayer) {
        this.targetLayer = targetLayer;
    }

    public boolean isMockMvcEnabled() {
        return mockMvcEnabled;
    }

    public void setMockMvcEnabled(boolean mockMvcEnabled) {
        this.mockMvcEnabled = mockMvcEnabled;
    }

    public boolean isGenerateEdgeCases() {
        return generateEdgeCases;
    }

    public void setGenerateEdgeCases(boolean generateEdgeCases) {
        this.generateEdgeCases = generateEdgeCases;
    }

    public List<String> getMethodsToTest() {
        return methodsToTest;
    }

    public void setMethodsToTest(List<String> methodsToTest) {
        this.methodsToTest = methodsToTest;
    }
}
