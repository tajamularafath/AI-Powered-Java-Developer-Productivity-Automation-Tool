package com.aiproductivity.java_dev_tool.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class ScaffoldWorkflowRequest {

    @NotBlank(message = "Entity name is required")
    private String entityName;

    private String basePackage = "com.example.demo";

    private String description = "Standard enterprise domain entity";

    private List<EntityFieldDefinition> fields = new ArrayList<>();

    private boolean generateTests = true;

    private boolean generateDdl = true;

    private boolean generateDto = true;

    private boolean generateService = true;

    private boolean generateController = true;

    private boolean runQualityAudit = true;

    public ScaffoldWorkflowRequest() {
    }

    public ScaffoldWorkflowRequest(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        this.entityName = entityName;
        this.basePackage = basePackage;
        this.fields = fields;
    }

    // Getters and Setters

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EntityFieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<EntityFieldDefinition> fields) {
        this.fields = fields;
    }

    public boolean isGenerateTests() {
        return generateTests;
    }

    public void setGenerateTests(boolean generateTests) {
        this.generateTests = generateTests;
    }

    public boolean isGenerateDdl() {
        return generateDdl;
    }

    public void setGenerateDdl(boolean generateDdl) {
        this.generateDdl = generateDdl;
    }

    public boolean isGenerateDto() {
        return generateDto;
    }

    public void setGenerateDto(boolean generateDto) {
        this.generateDto = generateDto;
    }

    public boolean isGenerateService() {
        return generateService;
    }

    public void setGenerateService(boolean generateService) {
        this.generateService = generateService;
    }

    public boolean isGenerateController() {
        return generateController;
    }

    public void setGenerateController(boolean generateController) {
        this.generateController = generateController;
    }

    public boolean isRunQualityAudit() {
        return runQualityAudit;
    }

    public void setRunQualityAudit(boolean runQualityAudit) {
        this.runQualityAudit = runQualityAudit;
    }
}
