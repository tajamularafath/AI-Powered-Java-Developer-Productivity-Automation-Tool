package com.aiproductivity.java_dev_tool.dto.response;

import com.aiproductivity.java_dev_tool.entity.ArtifactType;

public class GeneratedArtifactDto {
    private ArtifactType artifactType;
    private String fileName;
    private String packageName;
    private String content;
    private int lineCount;

    public GeneratedArtifactDto() {
    }

    public GeneratedArtifactDto(ArtifactType artifactType, String fileName, String packageName, String content) {
        this.artifactType = artifactType;
        this.fileName = fileName;
        this.packageName = packageName;
        this.content = content;
        this.lineCount = (content != null) ? content.split("\r\n|\r|\n").length : 0;
    }

    // Getters and Setters

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.lineCount = (content != null) ? content.split("\r\n|\r|\n").length : 0;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }
}
