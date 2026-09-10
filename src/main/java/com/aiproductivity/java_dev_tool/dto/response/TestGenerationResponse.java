package com.aiproductivity.java_dev_tool.dto.response;

import java.util.ArrayList;
import java.util.List;

public class TestGenerationResponse {
    private String testClassName;
    private String testPackage;
    private String generatedTestCode;
    private int generatedTestCasesCount;
    private List<String> coveredMethods = new ArrayList<>();
    private double estimatedMinutesSaved;

    public TestGenerationResponse() {
    }

    public TestGenerationResponse(String testClassName, String testPackage, String generatedTestCode,
                                  int generatedTestCasesCount, List<String> coveredMethods, double estimatedMinutesSaved) {
        this.testClassName = testClassName;
        this.testPackage = testPackage;
        this.generatedTestCode = generatedTestCode;
        this.generatedTestCasesCount = generatedTestCasesCount;
        this.coveredMethods = coveredMethods;
        this.estimatedMinutesSaved = estimatedMinutesSaved;
    }

    // Getters and Setters

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public String getTestPackage() {
        return testPackage;
    }

    public void setTestPackage(String testPackage) {
        this.testPackage = testPackage;
    }

    public String getGeneratedTestCode() {
        return generatedTestCode;
    }

    public void setGeneratedTestCode(String generatedTestCode) {
        this.generatedTestCode = generatedTestCode;
    }

    public int getGeneratedTestCasesCount() {
        return generatedTestCasesCount;
    }

    public void setGeneratedTestCasesCount(int generatedTestCasesCount) {
        this.generatedTestCasesCount = generatedTestCasesCount;
    }

    public List<String> getCoveredMethods() {
        return coveredMethods;
    }

    public void setCoveredMethods(List<String> coveredMethods) {
        this.coveredMethods = coveredMethods;
    }

    public double getEstimatedMinutesSaved() {
        return estimatedMinutesSaved;
    }

    public void setEstimatedMinutesSaved(double estimatedMinutesSaved) {
        this.estimatedMinutesSaved = estimatedMinutesSaved;
    }
}
