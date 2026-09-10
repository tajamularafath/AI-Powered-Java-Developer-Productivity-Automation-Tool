package com.aiproductivity.java_dev_tool.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CodeAuditRequest {

    private String className = "UnknownClass";

    @NotBlank(message = "Source code is required for code audit")
    private String sourceCode;

    private boolean checkSecurity = true;

    private boolean checkPerformance = true;

    private boolean checkSpringPatterns = true;

    private boolean checkModernJava = true;

    public CodeAuditRequest() {
    }

    public CodeAuditRequest(String className, String sourceCode) {
        this.className = className;
        this.sourceCode = sourceCode;
    }

    // Getters and Setters

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public boolean isCheckSecurity() {
        return checkSecurity;
    }

    public void setCheckSecurity(boolean checkSecurity) {
        this.checkSecurity = checkSecurity;
    }

    public boolean isCheckPerformance() {
        return checkPerformance;
    }

    public void setCheckPerformance(boolean checkPerformance) {
        this.checkPerformance = checkPerformance;
    }

    public boolean isCheckSpringPatterns() {
        return checkSpringPatterns;
    }

    public void setCheckSpringPatterns(boolean checkSpringPatterns) {
        this.checkSpringPatterns = checkSpringPatterns;
    }

    public boolean isCheckModernJava() {
        return checkModernJava;
    }

    public void setCheckModernJava(boolean checkModernJava) {
        this.checkModernJava = checkModernJava;
    }
}
