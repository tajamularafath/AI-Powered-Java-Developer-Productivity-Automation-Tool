package com.aiproductivity.java_dev_tool.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ModernizeCodeRequest {

    @NotBlank(message = "Source code is required")
    private String sourceCode;

    private int targetJavaVersion = 17;

    private boolean convertPojosToRecords = true;

    private boolean optimizeSwitchPatterns = true;

    private boolean modernizeStreams = true;

    private boolean modernizeSpringAnnotations = true;

    public ModernizeCodeRequest() {
    }

    public ModernizeCodeRequest(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    // Getters and Setters

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getTargetJavaVersion() {
        return targetJavaVersion;
    }

    public void setTargetJavaVersion(int targetJavaVersion) {
        this.targetJavaVersion = targetJavaVersion;
    }

    public boolean isConvertPojosToRecords() {
        return convertPojosToRecords;
    }

    public void setConvertPojosToRecords(boolean convertPojosToRecords) {
        this.convertPojosToRecords = convertPojosToRecords;
    }

    public boolean isOptimizeSwitchPatterns() {
        return optimizeSwitchPatterns;
    }

    public void setOptimizeSwitchPatterns(boolean optimizeSwitchPatterns) {
        this.optimizeSwitchPatterns = optimizeSwitchPatterns;
    }

    public boolean isModernizeStreams() {
        return modernizeStreams;
    }

    public void setModernizeStreams(boolean modernizeStreams) {
        this.modernizeStreams = modernizeStreams;
    }

    public boolean isModernizeSpringAnnotations() {
        return modernizeSpringAnnotations;
    }

    public void setModernizeSpringAnnotations(boolean modernizeSpringAnnotations) {
        this.modernizeSpringAnnotations = modernizeSpringAnnotations;
    }
}
