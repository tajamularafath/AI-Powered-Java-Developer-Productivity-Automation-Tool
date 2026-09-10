package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.request.DirectCodeGenRequest;
import com.aiproductivity.java_dev_tool.dto.request.EntityFieldDefinition;
import com.aiproductivity.java_dev_tool.dto.response.GeneratedArtifactDto;
import com.aiproductivity.java_dev_tool.entity.ArtifactType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeGenerationService {

    public GeneratedArtifactDto generateArtifact(DirectCodeGenRequest request) {
        String code;
        String fileName;
        String pkg = request.getBasePackage() != null ? request.getBasePackage() : "com.example.demo";
        String entityName = capitalize(request.getEntityName());
        List<EntityFieldDefinition> fields = request.getFields() != null ? request.getFields() : defaultFields(entityName);

        switch (request.getArtifactType()) {
            case ENTITY:
                code = generateEntityCode(entityName, pkg, fields);
                fileName = entityName + ".java";
                break;
            case REQUEST_DTO:
                code = generateRequestDtoCode(entityName, pkg, fields);
                fileName = entityName + "RequestDto.java";
                break;
            case RESPONSE_DTO:
                code = generateResponseDtoCode(entityName, pkg, fields);
                fileName = entityName + "ResponseDto.java";
                break;
            case REPOSITORY:
                code = generateRepositoryCode(entityName, pkg, fields);
                fileName = entityName + "Repository.java";
                break;
            case SERVICE:
                code = generateServiceCode(entityName, pkg, fields);
                fileName = entityName + "Service.java";
                break;
            case SERVICE_IMPL:
                code = generateServiceImplCode(entityName, pkg, fields);
                fileName = entityName + "ServiceImpl.java";
                break;
            case CONTROLLER:
                code = generateControllerCode(entityName, pkg, fields);
                fileName = entityName + "Controller.java";
                break;
            case DDL_MIGRATION:
                code = generateDdlScript(entityName, fields);
                fileName = "V1__create_" + toSnakeCase(entityName) + "_table.sql";
                break;
            default:
                code = generateEntityCode(entityName, pkg, fields);
                fileName = entityName + ".java";
        }
        return new GeneratedArtifactDto(request.getArtifactType(), fileName, pkg, code);
    }

    public String generateEntityCode(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        String tableName = toSnakeCase(pluralize(entityName));
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".entity;\n\n");
        sb.append("import jakarta.persistence.*;\n");
        sb.append("import jakarta.validation.constraints.*;\n");
        sb.append("import java.time.LocalDateTime;\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.util.Objects;\n\n");

        sb.append("/**\n");
        sb.append(" * ").append(entityName).append(" JPA Entity.\n");
        sb.append(" * Generated autonomously by AI Developer Productivity Tool.\n");
        sb.append(" */\n");
        sb.append("@Entity\n");
        sb.append("@Table(name = \"").append(tableName).append("\")\n");
        sb.append("public class ").append(entityName).append(" {\n\n");

        // Primary Key Field
        sb.append("    @Id\n");
        sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
        sb.append("    private Long id;\n\n");

        // Other Fields
        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String fieldType = resolveJavaType(f.getType());
            String colName = toSnakeCase(f.getName());

            if (!f.isNullable() && "String".equals(fieldType)) {
                sb.append("    @NotBlank(message = \"").append(f.getName()).append(" is required\")\n");
            } else if (!f.isNullable()) {
                sb.append("    @NotNull(message = \"").append(f.getName()).append(" cannot be null\")\n");
            }

            if (f.getValidationRules() != null && !f.getValidationRules().isBlank()) {
                sb.append("    ").append(f.getValidationRules().trim()).append("\n");
            }

            sb.append("    @Column(name = \"").append(colName).append("\"");
            if (!f.isNullable()) sb.append(", nullable = false");
            if (f.isUnique()) sb.append(", unique = true");
            sb.append(")\n");
            sb.append("    private ").append(fieldType).append(" ").append(f.getName()).append(";\n\n");
        }

        // Audit Timestamps
        sb.append("    @Column(name = \"created_at\", nullable = false, updatable = false)\n");
        sb.append("    private LocalDateTime createdAt = LocalDateTime.now();\n\n");
        sb.append("    @Column(name = \"updated_at\")\n");
        sb.append("    private LocalDateTime updatedAt = LocalDateTime.now();\n\n");

        // PreUpdate Hook
        sb.append("    @PreUpdate\n");
        sb.append("    protected void onUpdate() {\n");
        sb.append("        this.updatedAt = LocalDateTime.now();\n");
        sb.append("    }\n\n");

        // No-arg constructor
        sb.append("    public ").append(entityName).append("() {\n    }\n\n");

        // Getters and Setters
        sb.append("    public Long getId() {\n        return id;\n    }\n\n");
        sb.append("    public void setId(Long id) {\n        this.id = id;\n    }\n\n");

        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String fieldType = resolveJavaType(f.getType());
            String cap = capitalize(f.getName());
            sb.append("    public ").append(fieldType).append(" get").append(cap).append("() {\n");
            sb.append("        return ").append(f.getName()).append(";\n    }\n\n");
            sb.append("    public void set").append(cap).append("(").append(fieldType).append(" ").append(f.getName()).append(") {\n");
            sb.append("        this.").append(f.getName()).append(" = ").append(f.getName()).append(";\n    }\n\n");
        }

        sb.append("    public LocalDateTime getCreatedAt() {\n        return createdAt;\n    }\n\n");
        sb.append("    public void setCreatedAt(LocalDateTime createdAt) {\n        this.createdAt = createdAt;\n    }\n\n");
        sb.append("    public LocalDateTime getUpdatedAt() {\n        return updatedAt;\n    }\n\n");
        sb.append("    public void setUpdatedAt(LocalDateTime updatedAt) {\n        this.updatedAt = updatedAt;\n    }\n\n");

        // equals & hashCode
        sb.append("    @Override\n");
        sb.append("    public boolean equals(Object o) {\n");
        sb.append("        if (this == o) return true;\n");
        sb.append("        if (o == null || getClass() != o.getClass()) return false;\n");
        sb.append("        ").append(entityName).append(" that = (").append(entityName).append(") o;\n");
        sb.append("        return Objects.equals(id, that.id);\n");
        sb.append("    }\n\n");

        sb.append("    @Override\n");
        sb.append("    public int hashCode() {\n");
        sb.append("        return Objects.hash(id);\n");
        sb.append("    }\n");

        sb.append("}\n");
        return sb.toString();
    }

    public String generateRequestDtoCode(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".dto.request;\n\n");
        sb.append("import jakarta.validation.constraints.*;\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.time.LocalDateTime;\n\n");

        sb.append("/**\n");
        sb.append(" * Request DTO for creating or updating ").append(entityName).append(".\n");
        sb.append(" */\n");
        sb.append("public class ").append(entityName).append("RequestDto {\n\n");

        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String fieldType = resolveJavaType(f.getType());
            if (!f.isNullable() && "String".equals(fieldType)) {
                sb.append("    @NotBlank(message = \"").append(f.getName()).append(" is required\")\n");
            } else if (!f.isNullable()) {
                sb.append("    @NotNull(message = \"").append(f.getName()).append(" cannot be null\")\n");
            }
            if (f.getValidationRules() != null && !f.getValidationRules().isBlank()) {
                sb.append("    ").append(f.getValidationRules().trim()).append("\n");
            }
            sb.append("    private ").append(fieldType).append(" ").append(f.getName()).append(";\n\n");
        }

        sb.append("    public ").append(entityName).append("RequestDto() {\n    }\n\n");

        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String fieldType = resolveJavaType(f.getType());
            String cap = capitalize(f.getName());
            sb.append("    public ").append(fieldType).append(" get").append(cap).append("() {\n");
            sb.append("        return ").append(f.getName()).append(";\n    }\n\n");
            sb.append("    public void set").append(cap).append("(").append(fieldType).append(" ").append(f.getName()).append(") {\n");
            sb.append("        this.").append(f.getName()).append(" = ").append(f.getName()).append(";\n    }\n\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    public String generateResponseDtoCode(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".dto.response;\n\n");
        sb.append("import ").append(basePackage).append(".entity.").append(entityName).append(";\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.time.LocalDateTime;\n\n");

        sb.append("/**\n");
        sb.append(" * Response DTO for ").append(entityName).append(".\n");
        sb.append(" */\n");
        sb.append("public class ").append(entityName).append("ResponseDto {\n\n");
        sb.append("    private Long id;\n");

        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            sb.append("    private ").append(resolveJavaType(f.getType())).append(" ").append(f.getName()).append(";\n");
        }
        sb.append("    private LocalDateTime createdAt;\n");
        sb.append("    private LocalDateTime updatedAt;\n\n");

        sb.append("    public ").append(entityName).append("ResponseDto() {\n    }\n\n");

        // Static factory fromEntity method
        sb.append("    public static ").append(entityName).append("ResponseDto fromEntity(").append(entityName).append(" entity) {\n");
        sb.append("        if (entity == null) return null;\n");
        sb.append("        ").append(entityName).append("ResponseDto dto = new ").append(entityName).append("ResponseDto();\n");
        sb.append("        dto.setId(entity.getId());\n");
        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String cap = capitalize(f.getName());
            sb.append("        dto.set").append(cap).append("(entity.get").append(cap).append("());\n");
        }
        sb.append("        dto.setCreatedAt(entity.getCreatedAt());\n");
        sb.append("        dto.setUpdatedAt(entity.getUpdatedAt());\n");
        sb.append("        return dto;\n");
        sb.append("    }\n\n");

        // Getters and Setters
        sb.append("    public Long getId() {\n        return id;\n    }\n\n");
        sb.append("    public void setId(Long id) {\n        this.id = id;\n    }\n\n");
        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String fieldType = resolveJavaType(f.getType());
            String cap = capitalize(f.getName());
            sb.append("    public ").append(fieldType).append(" get").append(cap).append("() {\n");
            sb.append("        return ").append(f.getName()).append(";\n    }\n\n");
            sb.append("    public void set").append(cap).append("(").append(fieldType).append(" ").append(f.getName()).append(") {\n");
            sb.append("        this.").append(f.getName()).append(" = ").append(f.getName()).append(";\n    }\n\n");
        }
        sb.append("    public LocalDateTime getCreatedAt() {\n        return createdAt;\n    }\n\n");
        sb.append("    public void setCreatedAt(LocalDateTime createdAt) {\n        this.createdAt = createdAt;\n    }\n\n");
        sb.append("    public LocalDateTime getUpdatedAt() {\n        return updatedAt;\n    }\n\n");
        sb.append("    public void setUpdatedAt(LocalDateTime updatedAt) {\n        this.updatedAt = updatedAt;\n    }\n\n");
        sb.append("}\n");
        return sb.toString();
    }

    public String generateRepositoryCode(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".repository;\n\n");
        sb.append("import ").append(basePackage).append(".entity.").append(entityName).append(";\n");
        sb.append("import org.springframework.data.domain.Page;\n");
        sb.append("import org.springframework.data.domain.Pageable;\n");
        sb.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        sb.append("import org.springframework.data.jpa.repository.Query;\n");
        sb.append("import org.springframework.data.repository.query.Param;\n");
        sb.append("import org.springframework.stereotype.Repository;\n\n");
        sb.append("import java.util.Optional;\n\n");

        sb.append("/**\n");
        sb.append(" * Spring Data JPA Repository for {@link ").append(entityName).append("}.\n");
        sb.append(" */\n");
        sb.append("@Repository\n");
        sb.append("public interface ").append(entityName).append("Repository extends JpaRepository<").append(entityName).append(", Long> {\n\n");

        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            if (f.isUnique()) {
                String cap = capitalize(f.getName());
                sb.append("    Optional<").append(entityName).append("> findBy").append(cap).append("(")
                        .append(resolveJavaType(f.getType())).append(" ").append(f.getName()).append(");\n\n");
                sb.append("    boolean existsBy").append(cap).append("(")
                        .append(resolveJavaType(f.getType())).append(" ").append(f.getName()).append(");\n\n");
            }
        }

        sb.append("    Page<").append(entityName).append("> findAllByOrderByCreatedAtDesc(Pageable pageable);\n");
        sb.append("}\n");
        return sb.toString();
    }

    public String generateServiceCode(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".service;\n\n");
        sb.append("import ").append(basePackage).append(".dto.request.").append(entityName).append("RequestDto;\n");
        sb.append("import ").append(basePackage).append(".dto.response.").append(entityName).append("ResponseDto;\n");
        sb.append("import org.springframework.data.domain.Page;\n");
        sb.append("import org.springframework.data.domain.Pageable;\n\n");

        sb.append("public interface ").append(entityName).append("Service {\n\n");
        sb.append("    ").append(entityName).append("ResponseDto create(").append(entityName).append("RequestDto requestDto);\n\n");
        sb.append("    ").append(entityName).append("ResponseDto findById(Long id);\n\n");
        sb.append("    Page<").append(entityName).append("ResponseDto> findAll(Pageable pageable);\n\n");
        sb.append("    ").append(entityName).append("ResponseDto update(Long id, ").append(entityName).append("RequestDto requestDto);\n\n");
        sb.append("    void delete(Long id);\n");
        sb.append("}\n");
        return sb.toString();
    }

    public String generateServiceImplCode(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        String repoVar = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1) + "Repository";
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".service.impl;\n\n");
        sb.append("import ").append(basePackage).append(".entity.").append(entityName).append(";\n");
        sb.append("import ").append(basePackage).append(".dto.request.").append(entityName).append("RequestDto;\n");
        sb.append("import ").append(basePackage).append(".dto.response.").append(entityName).append("ResponseDto;\n");
        sb.append("import ").append(basePackage).append(".repository.").append(entityName).append("Repository;\n");
        sb.append("import ").append(basePackage).append(".service.").append(entityName).append("Service;\n");
        sb.append("import org.slf4j.Logger;\n");
        sb.append("import org.slf4j.LoggerFactory;\n");
        sb.append("import org.springframework.data.domain.Page;\n");
        sb.append("import org.springframework.data.domain.Pageable;\n");
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("import org.springframework.transaction.annotation.Transactional;\n\n");

        sb.append("@Service\n");
        sb.append("@Transactional\n");
        sb.append("public class ").append(entityName).append("ServiceImpl implements ").append(entityName).append("Service {\n\n");
        sb.append("    private static final Logger log = LoggerFactory.getLogger(").append(entityName).append("ServiceImpl.class);\n\n");
        sb.append("    private final ").append(entityName).append("Repository ").append(repoVar).append(";\n\n");

        // Constructor Injection
        sb.append("    public ").append(entityName).append("ServiceImpl(").append(entityName).append("Repository ").append(repoVar).append(") {\n");
        sb.append("        this.").append(repoVar).append(" = ").append(repoVar).append(";\n");
        sb.append("    }\n\n");

        // create
        sb.append("    @Override\n");
        sb.append("    public ").append(entityName).append("ResponseDto create(").append(entityName).append("RequestDto requestDto) {\n");
        sb.append("        log.info(\"Creating new ").append(entityName).append("\");\n");
        sb.append("        ").append(entityName).append(" entity = new ").append(entityName).append("();\n");
        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String cap = capitalize(f.getName());
            sb.append("        entity.set").append(cap).append("(requestDto.get").append(cap).append("());\n");
        }
        sb.append("        ").append(entityName).append(" saved = ").append(repoVar).append(".save(entity);\n");
        sb.append("        return ").append(entityName).append("ResponseDto.fromEntity(saved);\n");
        sb.append("    }\n\n");

        // findById
        sb.append("    @Override\n");
        sb.append("    @Transactional(readOnly = true)\n");
        sb.append("    public ").append(entityName).append("ResponseDto findById(Long id) {\n");
        sb.append("        log.info(\"Fetching ").append(entityName).append(" with ID: {}\", id);\n");
        sb.append("        return ").append(repoVar).append(".findById(id)\n");
        sb.append("                .map(").append(entityName).append("ResponseDto::fromEntity)\n");
        sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(entityName).append(" not found with id: \" + id));\n");
        sb.append("    }\n\n");

        // findAll
        sb.append("    @Override\n");
        sb.append("    @Transactional(readOnly = true)\n");
        sb.append("    public Page<").append(entityName).append("ResponseDto> findAll(Pageable pageable) {\n");
        sb.append("        return ").append(repoVar).append(".findAll(pageable).map(").append(entityName).append("ResponseDto::fromEntity);\n");
        sb.append("    }\n\n");

        // update
        sb.append("    @Override\n");
        sb.append("    public ").append(entityName).append("ResponseDto update(Long id, ").append(entityName).append("RequestDto requestDto) {\n");
        sb.append("        log.info(\"Updating ").append(entityName).append(" with ID: {}\", id);\n");
        sb.append("        ").append(entityName).append(" entity = ").append(repoVar).append(".findById(id)\n");
        sb.append("                .orElseThrow(() -> new RuntimeException(\"").append(entityName).append(" not found with id: \" + id));\n\n");
        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String cap = capitalize(f.getName());
            sb.append("        entity.set").append(cap).append("(requestDto.get").append(cap).append("());\n");
        }
        sb.append("        return ").append(entityName).append("ResponseDto.fromEntity(entity);\n");
        sb.append("    }\n\n");

        // delete
        sb.append("    @Override\n");
        sb.append("    public void delete(Long id) {\n");
        sb.append("        log.info(\"Deleting ").append(entityName).append(" with ID: {}\", id);\n");
        sb.append("        if (!").append(repoVar).append(".existsById(id)) {\n");
        sb.append("            throw new RuntimeException(\"").append(entityName).append(" not found with id: \" + id);\n");
        sb.append("        }\n");
        sb.append("        ").append(repoVar).append(".deleteById(id);\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    public String generateControllerCode(String entityName, String basePackage, List<EntityFieldDefinition> fields) {
        String serviceVar = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1) + "Service";
        String path = toKebabCase(pluralize(entityName));
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(basePackage).append(".controller;\n\n");
        sb.append("import ").append(basePackage).append(".dto.request.").append(entityName).append("RequestDto;\n");
        sb.append("import ").append(basePackage).append(".dto.response.").append(entityName).append("ResponseDto;\n");
        sb.append("import ").append(basePackage).append(".service.").append(entityName).append("Service;\n");
        sb.append("import jakarta.validation.Valid;\n");
        sb.append("import org.springframework.data.domain.Page;\n");
        sb.append("import org.springframework.data.domain.Pageable;\n");
        sb.append("import org.springframework.http.HttpStatus;\n");
        sb.append("import org.springframework.http.ResponseEntity;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n\n");

        sb.append("/**\n");
        sb.append(" * REST Controller for managing ").append(entityName).append(" resources.\n");
        sb.append(" */\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/api/v1/").append(path).append("\")\n");
        sb.append("@CrossOrigin(origins = \"*\")\n");
        sb.append("public class ").append(entityName).append("Controller {\n\n");

        sb.append("    private final ").append(entityName).append("Service ").append(serviceVar).append(";\n\n");
        sb.append("    public ").append(entityName).append("Controller(").append(entityName).append("Service ").append(serviceVar).append(") {\n");
        sb.append("        this.").append(serviceVar).append(" = ").append(serviceVar).append(";\n");
        sb.append("    }\n\n");

        // POST create
        sb.append("    @PostMapping\n");
        sb.append("    public ResponseEntity<").append(entityName).append("ResponseDto> create(@Valid @RequestBody ").append(entityName).append("RequestDto requestDto) {\n");
        sb.append("        ").append(entityName).append("ResponseDto created = ").append(serviceVar).append(".create(requestDto);\n");
        sb.append("        return ResponseEntity.status(HttpStatus.CREATED).body(created);\n");
        sb.append("    }\n\n");

        // GET by ID
        sb.append("    @GetMapping(\"/{id}\")\n");
        sb.append("    public ResponseEntity<").append(entityName).append("ResponseDto> getById(@PathVariable Long id) {\n");
        sb.append("        return ResponseEntity.ok(").append(serviceVar).append(".findById(id));\n");
        sb.append("    }\n\n");

        // GET all (paginated)
        sb.append("    @GetMapping\n");
        sb.append("    public ResponseEntity<Page<").append(entityName).append("ResponseDto>> getAll(Pageable pageable) {\n");
        sb.append("        return ResponseEntity.ok(").append(serviceVar).append(".findAll(pageable));\n");
        sb.append("    }\n\n");

        // PUT update
        sb.append("    @PutMapping(\"/{id}\")\n");
        sb.append("    public ResponseEntity<").append(entityName).append("ResponseDto> update(@PathVariable Long id, @Valid @RequestBody ").append(entityName).append("RequestDto requestDto) {\n");
        sb.append("        return ResponseEntity.ok(").append(serviceVar).append(".update(id, requestDto));\n");
        sb.append("    }\n\n");

        // DELETE
        sb.append("    @DeleteMapping(\"/{id}\")\n");
        sb.append("    public ResponseEntity<Void> delete(@PathVariable Long id) {\n");
        sb.append("        ").append(serviceVar).append(".delete(id);\n");
        sb.append("        return ResponseEntity.noContent().build();\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    public String generateDdlScript(String entityName, List<EntityFieldDefinition> fields) {
        String tableName = toSnakeCase(pluralize(entityName));
        StringBuilder sb = new StringBuilder();
        sb.append("-- MySQL DDL Migration for ").append(entityName).append("\n");
        sb.append("-- Generated autonomously by AI Developer Productivity Tool\n\n");
        sb.append("CREATE TABLE IF NOT EXISTS `").append(tableName).append("` (\n");
        sb.append("    `id` BIGINT NOT NULL AUTO_INCREMENT,\n");

        for (EntityFieldDefinition f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) continue;
            String colName = toSnakeCase(f.getName());
            String mySqlType = resolveMySqlType(f.getType());
            sb.append("    `").append(colName).append("` ").append(mySqlType);
            if (!f.isNullable()) sb.append(" NOT NULL");
            if (f.isUnique()) sb.append(" UNIQUE");
            sb.append(",\n");
        }

        sb.append("    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n");
        sb.append("    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n");
        sb.append("    PRIMARY KEY (`id`)\n");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n\n");

        // Add index on unique or foreign key fields
        for (EntityFieldDefinition f : fields) {
            if (f.getName().toLowerCase().endsWith("id") && !"id".equalsIgnoreCase(f.getName())) {
                sb.append("CREATE INDEX `idx_").append(tableName).append("_").append(toSnakeCase(f.getName()))
                        .append("` ON `").append(tableName).append("` (`").append(toSnakeCase(f.getName())).append("`);\n");
            }
        }
        return sb.toString();
    }

    private List<EntityFieldDefinition> defaultFields(String entityName) {
        List<EntityFieldDefinition> list = new ArrayList<>();
        list.add(new EntityFieldDefinition("name", "String", false, false, "@NotBlank", "Entity name", "Alpha Product"));
        list.add(new EntityFieldDefinition("description", "String", false, true, "@Size(max=500)", "Detailed description", "Production sample"));
        list.add(new EntityFieldDefinition("status", "String", false, false, "@NotBlank", "Current status", "ACTIVE"));
        return list;
    }

    private String resolveJavaType(String type) {
        if (type == null) return "String";
        return switch (type.toLowerCase()) {
            case "int", "integer" -> "Integer";
            case "long" -> "Long";
            case "double" -> "Double";
            case "boolean", "bool" -> "Boolean";
            case "bigdecimal", "decimal", "price", "amount" -> "BigDecimal";
            case "localdate", "date" -> "java.time.LocalDate";
            case "localdatetime", "datetime", "timestamp" -> "LocalDateTime";
            default -> "String";
        };
    }

    private String resolveMySqlType(String type) {
        if (type == null) return "VARCHAR(255)";
        return switch (type.toLowerCase()) {
            case "int", "integer" -> "INT";
            case "long" -> "BIGINT";
            case "double" -> "DOUBLE";
            case "boolean", "bool" -> "BOOLEAN";
            case "bigdecimal", "decimal", "price", "amount" -> "DECIMAL(19, 4)";
            case "localdate", "date" -> "DATE";
            case "localdatetime", "datetime", "timestamp" -> "TIMESTAMP";
            case "text", "description" -> "TEXT";
            default -> "VARCHAR(255)";
        };
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private String toSnakeCase(String str) {
        if (str == null) return "";
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    private String toKebabCase(String str) {
        if (str == null) return "";
        return str.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }

    private String pluralize(String name) {
        if (name.endsWith("y")) return name.substring(0, name.length() - 1) + "ies";
        if (name.endsWith("s") || name.endsWith("sh") || name.endsWith("ch") || name.endsWith("x")) return name + "es";
        return name + "s";
    }
}
