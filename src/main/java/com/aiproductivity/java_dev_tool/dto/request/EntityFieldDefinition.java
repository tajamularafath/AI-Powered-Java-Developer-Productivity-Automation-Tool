package com.aiproductivity.java_dev_tool.dto.request;

public class EntityFieldDefinition {
    private String name;
    private String type; // e.g. String, Long, Integer, BigDecimal, Boolean, LocalDate, LocalDateTime
    private boolean primaryKey = false;
    private boolean nullable = true;
    private boolean unique = false;
    private String validationRules; // e.g. @NotBlank, @Size(min=2, max=100), @Min(0), @Email
    private String description;
    private String exampleValue;

    public EntityFieldDefinition() {
    }

    public EntityFieldDefinition(String name, String type, boolean primaryKey, boolean nullable) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
        this.nullable = nullable;
    }

    public EntityFieldDefinition(String name, String type, boolean primaryKey, boolean nullable, String validationRules, String description, String exampleValue) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
        this.nullable = nullable;
        this.validationRules = validationRules;
        this.description = description;
        this.exampleValue = exampleValue;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getValidationRules() {
        return validationRules;
    }

    public void setValidationRules(String validationRules) {
        this.validationRules = validationRules;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExampleValue() {
        return exampleValue;
    }

    public void setExampleValue(String exampleValue) {
        this.exampleValue = exampleValue;
    }
}
