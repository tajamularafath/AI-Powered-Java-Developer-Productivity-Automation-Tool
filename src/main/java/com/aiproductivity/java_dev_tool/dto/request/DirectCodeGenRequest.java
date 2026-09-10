package com.aiproductivity.java_dev_tool.dto.request;

import com.aiproductivity.java_dev_tool.entity.ArtifactType;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class DirectCodeGenRequest {

    @NotBlank(message = "Entity name is required")
    private String entityName;

    private String basePackage = "com.example.demo";

    private ArtifactType artifactType = ArtifactType.ENTITY;

    private List<EntityFieldDefinition> fields = new ArrayList<>();

    public DirectCodeGenRequest() {
    }

    public DirectCodeGenRequest(String entityName, String basePackage, ArtifactType artifactType, List<EntityFieldDefinition> fields) {
        this.entityName = entityName;
        this.basePackage = basePackage;
        this.artifactType = artifactType;
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

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    public List<EntityFieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<EntityFieldDefinition> fields) {
        this.fields = fields;
    }
}
