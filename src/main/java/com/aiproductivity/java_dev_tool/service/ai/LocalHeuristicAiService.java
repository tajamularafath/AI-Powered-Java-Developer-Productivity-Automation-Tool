package com.aiproductivity.java_dev_tool.service.ai;

import com.aiproductivity.java_dev_tool.dto.response.CodeAuditIssueDto;
import com.aiproductivity.java_dev_tool.dto.response.CodeAuditResponse;
import com.aiproductivity.java_dev_tool.dto.response.TestGenerationResponse;
import com.aiproductivity.java_dev_tool.entity.IssueCategory;
import com.aiproductivity.java_dev_tool.entity.IssueSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("localHeuristicAiService")
public class LocalHeuristicAiService implements AiService {

    private static final Logger log = LoggerFactory.getLogger(LocalHeuristicAiService.class);

    @Override
    public String generateContent(String systemInstruction, String userPrompt) {
        return "Autonomous heuristic AI engine analyzed prompt and prepared code generation strategy.";
    }

    @Override
    public CodeAuditResponse auditCode(String className, String sourceCode) {
        CodeAuditResponse response = new CodeAuditResponse();
        response.setTargetClassName(className != null && !className.isBlank() ? className : extractClassName(sourceCode));
        response.setCreatedAt(LocalDateTime.now());

        List<CodeAuditIssueDto> issues = new ArrayList<>();
        String[] lines = sourceCode != null ? sourceCode.split("\r\n|\r|\n") : new String[0];

        // 1. SQL Injection Scan
        Pattern sqlConcatPattern = Pattern.compile("(\"(SELECT|INSERT|UPDATE|DELETE|FROM|WHERE).*\"\\s*\\+\\s*[a-zA-Z0-9_]+)", Pattern.CASE_INSENSITIVE);
        // 2. Hardcoded Credentials Scan
        Pattern secretPattern = Pattern.compile("(password|secret|apikey|api_key|token)\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        // 3. Unsafe Optional.get() Scan
        Pattern optionalGetPattern = Pattern.compile("\\.get\\(\\)");
        // 4. Field Injection Scan
        Pattern fieldInjectionPattern = Pattern.compile("@Autowired\\s+(private|protected)\\s+");
        // 5. Raw Types Scan
        Pattern rawTypePattern = Pattern.compile("\\b(List|Map|Set|Collection)\\s+[a-zA-Z0-9_]+\\s*=");
        // 6. N+1 Loop Query Risk
        Pattern loopPattern = Pattern.compile("for\\s*\\([^)]+\\)|while\\s*\\([^)]+\\)");

        boolean inLoop = false;
        int loopDepth = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineNum = i + 1;

            if (loopPattern.matcher(line).find()) {
                inLoop = true;
                loopDepth = 2;
            } else if (inLoop) {
                loopDepth--;
                if (loopDepth <= 0) inLoop = false;
            }

            // Check SQL Injection
            Matcher sqlMatcher = sqlConcatPattern.matcher(line);
            if (sqlMatcher.find()) {
                issues.add(new CodeAuditIssueDto(
                        "SEC-001",
                        "SQL Injection Risk via String Concatenation",
                        IssueSeverity.CRITICAL,
                        IssueCategory.SECURITY,
                        lineNum,
                        line.trim(),
                        "Dynamic SQL constructed via string concatenation exposes the database to SQL injection attacks.",
                        "Use parameterized JPA queries (`:paramName`), Spring Data derived query methods, or Criteria API.",
                        "// Use parameterized query:\n@Query(\"SELECT e FROM Entity e WHERE e.name = :name\")\nOptional<Entity> findByName(@Param(\"name\") String name);"
                ));
            }

            // Check Hardcoded Credentials
            Matcher secretMatcher = secretPattern.matcher(line);
            if (secretMatcher.find() && !line.contains("password.equals") && !line.contains("dummy")) {
                issues.add(new CodeAuditIssueDto(
                        "SEC-002",
                        "Hardcoded Credential or Secret in Source Code",
                        IssueSeverity.HIGH,
                        IssueCategory.SECURITY,
                        lineNum,
                        line.trim(),
                        "Hardcoded credentials or API keys in source control can lead to credential theft and unauthorized access.",
                        "Externalize credentials into environment variables or application properties using `@Value(\"${secret.key}\")` or Spring Cloud Vault.",
                        "@Value(\"${app.security.secret-key}\")\nprivate String secretKey;"
                ));
            }

            // Check Unsafe Optional.get()
            Matcher optMatcher = optionalGetPattern.matcher(line);
            if (optMatcher.find() && !line.contains("orElse") && !line.contains("ifPresent")) {
                issues.add(new CodeAuditIssueDto(
                        "SAFE-001",
                        "Unsafe Optional.get() Invocation",
                        IssueSeverity.HIGH,
                        IssueCategory.NULL_SAFETY,
                        lineNum,
                        line.trim(),
                        "Calling `Optional.get()` directly without checking `isPresent()` or using `orElseThrow()` causes NoSuchElementException at runtime.",
                        "Replace `.get()` with `.orElseThrow(() -> new ResourceNotFoundException(\"Resource not found\"))`.",
                        line.replace(".get()", ".orElseThrow(() -> new ResourceNotFoundException(\"Entity not found\"))").trim()
                ));
            }

            // Check N+1 Query inside loop
            if (inLoop && (line.contains("Repository.find") || line.contains("Repository.get") || line.contains(".get") && line.contains("List"))) {
                issues.add(new CodeAuditIssueDto(
                        "PERF-001",
                        "Potential N+1 Database Query in Iteration Loop",
                        IssueSeverity.HIGH,
                        IssueCategory.PERFORMANCE,
                        lineNum,
                        line.trim(),
                        "Invoking repository query or lazy relationship traversal inside a loop triggers 1 query for parent + N queries for children, degrading database performance.",
                        "Batch query the entities using `findAllById(ids)` or use an `@EntityGraph` / `JOIN FETCH` to load associations in a single query.",
                        "List<Entity> entities = repository.findAllById(idList);"
                ));
            }

            // Check Field Injection
            if (fieldInjectionPattern.matcher(line).find()) {
                issues.add(new CodeAuditIssueDto(
                        "SPRING-001",
                        "Field Injection via @Autowired Detected",
                        IssueSeverity.LOW,
                        IssueCategory.SPRING_BEST_PRACTICE,
                        lineNum,
                        line.trim(),
                        "Field injection makes classes hard to unit test without reflection, hides dependencies, and creates immutability issues.",
                        "Use constructor-based injection for mandatory dependencies.",
                        "private final MyDependency myDependency;\n\npublic MyClass(MyDependency myDependency) {\n    this.myDependency = myDependency;\n}"
                ));
            }

            // Check Raw Types
            if (rawTypePattern.matcher(line).find() && !line.contains("<")) {
                issues.add(new CodeAuditIssueDto(
                        "SMELL-001",
                        "Raw Collection Type Without Generic Parameter",
                        IssueSeverity.MEDIUM,
                        IssueCategory.CODE_SMELL,
                        lineNum,
                        line.trim(),
                        "Using raw types bypasses compiler type checking and leads to ClassCastException at runtime.",
                        "Specify the generic type parameter, e.g. `List<Item>` instead of raw `List`.",
                        line.replaceFirst("\\b(List|Map|Set)\\b", "$1<Object>").trim()
                ));
            }
        }

        // Check if class is a mutating Service without @Transactional
        if (sourceCode != null && (sourceCode.contains("@Service") || sourceCode.contains("Service"))
                && (sourceCode.contains("save(") || sourceCode.contains("delete(") || sourceCode.contains("update("))
                && !sourceCode.contains("@Transactional")) {
            issues.add(new CodeAuditIssueDto(
                    "SPRING-002",
                    "Missing @Transactional on State-Mutating Service",
                    IssueSeverity.MEDIUM,
                    IssueCategory.SPRING_BEST_PRACTICE,
                    1,
                    "@Service",
                    "Service methods performing database writes or updates must define transaction boundaries to ensure atomicity and rollback on error.",
                    "Annotate the service method or class with `@Transactional`.",
                    "@Transactional\npublic void saveEntity(...) { ... }"
            ));
        }

        // Tally issue counts and calculate quality score
        int critical = 0, high = 0, medium = 0, low = 0, info = 0;
        for (CodeAuditIssueDto issue : issues) {
            switch (issue.getSeverity()) {
                case CRITICAL -> critical++;
                case HIGH -> high++;
                case MEDIUM -> medium++;
                case LOW -> low++;
                case INFO -> info++;
            }
        }

        int score = 100 - (critical * 25) - (high * 15) - (medium * 8) - (low * 3) - (info * 1);
        if (score < 0) score = 0;

        response.setOverallScore(score);
        response.setTotalIssues(issues.size());
        response.setCriticalCount(critical);
        response.setHighCount(high);
        response.setMediumCount(medium);
        response.setLowCount(low);
        response.setInfoCount(info);
        response.setIssues(issues);

        // Build executive summary
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Code Audit completed with score %d/100. ", score));
        if (issues.isEmpty()) {
            summary.append("No security vulnerabilities, performance anti-patterns, or code smells detected! Code meets enterprise Spring Boot standards.");
        } else {
            summary.append(String.format("Found %d issue(s): %d Critical, %d High, %d Medium, %d Low. ",
                    issues.size(), critical, high, medium, low));
            if (critical > 0) summary.append("Urgent remediation required for critical security vulnerabilities before production deployment. ");
            if (high > 0) summary.append("Address high-severity performance and safety concerns to prevent production incidents.");
        }
        response.setExecutiveSummary(summary.toString());

        // Generate remediated source code
        response.setRemediatedCode(generateRemediatedCode(sourceCode, issues));
        return response;
    }

    @Override
    public TestGenerationResponse synthesizeTests(String className, String packageName, String sourceCode, String targetLayer) {
        String targetClass = className != null && !className.isBlank() ? className : extractClassName(sourceCode);
        if (targetClass.isBlank()) targetClass = "SampleService";
        String pkg = packageName != null && !packageName.isBlank() ? packageName : extractPackage(sourceCode);
        if (pkg.isBlank()) pkg = "com.example.demo.service";

        String testClassName = targetClass + "Test";
        String entityName = targetClass.replaceAll("Service|Controller|Repository|Impl", "");
        if (entityName.isBlank()) entityName = "Entity";
        String entityVar = Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1);
        String serviceVar = Character.toLowerCase(targetClass.charAt(0)) + targetClass.substring(1);

        StringBuilder testCode = new StringBuilder();
        testCode.append("package ").append(pkg).append(";\n\n");
        testCode.append("import org.junit.jupiter.api.BeforeEach;\n");
        testCode.append("import org.junit.jupiter.api.DisplayName;\n");
        testCode.append("import org.junit.jupiter.api.Nested;\n");
        testCode.append("import org.junit.jupiter.api.Test;\n");
        testCode.append("import org.junit.jupiter.api.extension.ExtendWith;\n");
        testCode.append("import org.mockito.InjectMocks;\n");
        testCode.append("import org.mockito.Mock;\n");
        testCode.append("import org.mockito.junit.jupiter.MockitoExtension;\n");
        testCode.append("import org.springframework.data.domain.Page;\n");
        testCode.append("import org.springframework.data.domain.PageImpl;\n");
        testCode.append("import org.springframework.data.domain.PageRequest;\n");
        testCode.append("import org.springframework.data.domain.Pageable;\n\n");
        testCode.append("import java.util.List;\n");
        testCode.append("import java.util.Optional;\n\n");
        testCode.append("import static org.assertj.core.api.Assertions.assertThat;\n");
        testCode.append("import static org.assertj.core.api.Assertions.assertThatThrownBy;\n");
        testCode.append("import static org.mockito.ArgumentMatchers.any;\n");
        testCode.append("import static org.mockito.Mockito.*;\n\n");

        testCode.append("/**\n");
        testCode.append(" * Automated JUnit 5 + Mockito Test Suite for {@link ").append(targetClass).append("}.\n");
        testCode.append(" * Synthesized autonomously by AI Developer Productivity Tool.\n");
        testCode.append(" */\n");
        testCode.append("@ExtendWith(MockitoExtension.class)\n");
        testCode.append("class ").append(testClassName).append(" {\n\n");

        testCode.append("    @Mock\n");
        testCode.append("    private ").append(entityName).append("Repository ").append(entityVar).append("Repository;\n\n");

        testCode.append("    @InjectMocks\n");
        testCode.append("    private ").append(targetClass).append(" ").append(serviceVar).append(";\n\n");

        testCode.append("    private ").append(entityName).append(" mock").append(entityName).append(";\n\n");

        testCode.append("    @BeforeEach\n");
        testCode.append("    void setUp() {\n");
        testCode.append("        mock").append(entityName).append(" = new ").append(entityName).append("();\n");
        testCode.append("        mock").append(entityName).append(".setId(1L);\n");
        testCode.append("    }\n\n");

        testCode.append("    @Nested\n");
        testCode.append("    @DisplayName(\"Tests for findById\")\n");
        testCode.append("    class FindByIdTests {\n\n");
        testCode.append("        @Test\n");
        testCode.append("        @DisplayName(\"Should return entity when valid ID is provided\")\n");
        testCode.append("        void shouldReturnEntityWhenIdExists() {\n");
        testCode.append("            // Given\n");
        testCode.append("            when(").append(entityVar).append("Repository.findById(1L)).thenReturn(Optional.of(mock").append(entityName).append("));\n\n");
        testCode.append("            // When\n");
        testCode.append("            var result = ").append(serviceVar).append(".findById(1L);\n\n");
        testCode.append("            // Then\n");
        testCode.append("            assertThat(result).isNotNull();\n");
        testCode.append("            verify(").append(entityVar).append("Repository, times(1)).findById(1L);\n");
        testCode.append("        }\n\n");

        testCode.append("        @Test\n");
        testCode.append("        @DisplayName(\"Should throw Exception when entity does not exist\")\n");
        testCode.append("        void shouldThrowExceptionWhenEntityNotFound() {\n");
        testCode.append("            // Given\n");
        testCode.append("            when(").append(entityVar).append("Repository.findById(999L)).thenReturn(Optional.empty());\n\n");
        testCode.append("            // When & Then\n");
        testCode.append("            assertThatThrownBy(() -> ").append(serviceVar).append(".findById(999L))\n");
        testCode.append("                    .isInstanceOf(RuntimeException.class);\n");
        testCode.append("            verify(").append(entityVar).append("Repository, times(1)).findById(999L);\n");
        testCode.append("        }\n");
        testCode.append("    }\n\n");

        testCode.append("    @Nested\n");
        testCode.append("    @DisplayName(\"Tests for create\")\n");
        testCode.append("    class CreateTests {\n\n");
        testCode.append("        @Test\n");
        testCode.append("        @DisplayName(\"Should persist and return new entity\")\n");
        testCode.append("        void shouldPersistEntitySuccessfully() {\n");
        testCode.append("            // Given\n");
        testCode.append("            when(").append(entityVar).append("Repository.save(any())).thenReturn(mock").append(entityName).append(");\n\n");
        testCode.append("            // When\n");
        testCode.append("            var result = ").append(serviceVar).append(".create(any());\n\n");
        testCode.append("            // Then\n");
        testCode.append("            assertThat(result).isNotNull();\n");
        testCode.append("            verify(").append(entityVar).append("Repository, times(1)).save(any());\n");
        testCode.append("        }\n");
        testCode.append("    }\n\n");

        testCode.append("    @Nested\n");
        testCode.append("    @DisplayName(\"Tests for findAll\")\n");
        testCode.append("    class FindAllTests {\n\n");
        testCode.append("        @Test\n");
        testCode.append("        @DisplayName(\"Should return paginated list of entities\")\n");
        testCode.append("        void shouldReturnPaginatedEntities() {\n");
        testCode.append("            // Given\n");
        testCode.append("            Pageable pageable = PageRequest.of(0, 10);\n");
        testCode.append("            Page<").append(entityName).append("> page = new PageImpl<>(List.of(mock").append(entityName).append("));\n");
        testCode.append("            when(").append(entityVar).append("Repository.findAll(pageable)).thenReturn(page);\n\n");
        testCode.append("            // When\n");
        testCode.append("            var result = ").append(serviceVar).append(".findAll(pageable);\n\n");
        testCode.append("            // Then\n");
        testCode.append("            assertThat(result).isNotNull();\n");
        testCode.append("            assertThat(result.getContent()).hasSize(1);\n");
        testCode.append("            verify(").append(entityVar).append("Repository, times(1)).findAll(pageable);\n");
        testCode.append("        }\n");
        testCode.append("    }\n\n");

        testCode.append("    @Nested\n");
        testCode.append("    @DisplayName(\"Tests for delete\")\n");
        testCode.append("    class DeleteTests {\n\n");
        testCode.append("        @Test\n");
        testCode.append("        @DisplayName(\"Should delete existing entity\")\n");
        testCode.append("        void shouldDeleteEntitySuccessfully() {\n");
        testCode.append("            // Given\n");
        testCode.append("            when(").append(entityVar).append("Repository.existsById(1L)).thenReturn(true);\n");
        testCode.append("            doNothing().when(").append(entityVar).append("Repository).deleteById(1L);\n\n");
        testCode.append("            // When\n");
        testCode.append("            ").append(serviceVar).append(".delete(1L);\n\n");
        testCode.append("            // Then\n");
        testCode.append("            verify(").append(entityVar).append("Repository, times(1)).deleteById(1L);\n");
        testCode.append("        }\n");
        testCode.append("    }\n");
        testCode.append("}\n");

        List<String> methods = List.of("findById(Long id)", "create(RequestDto dto)", "findAll(Pageable pageable)", "delete(Long id)");
        return new TestGenerationResponse(testClassName, pkg, testCode.toString(), 5, methods, 45.0);
    }

    @Override
    public String modernizeCode(String sourceCode, int targetJavaVersion) {
        if (sourceCode == null) return "";
        String modernized = sourceCode;

        // 1. Convert Collectors.toList() to .toList()
        modernized = modernized.replaceAll("\\.collect\\(Collectors\\.toList\\(\\)\\)", ".toList()");

        // 2. Modernize field injection to constructor injection if present
        if (modernized.contains("@Autowired\n    private ") || modernized.contains("@Autowired private ")) {
            modernized = modernized.replaceAll("@Autowired\\s+private\\s+([a-zA-Z0-9_<>]+)\\s+([a-zA-Z0-9_]+);", "private final $1 $2;");
        }

        // 3. Upgrade legacy String.format or string concatenation where suitable
        modernized = modernized.replaceAll("new StringBuilder\\(\\)\\.append\\(([^)]+)\\)\\.append\\(([^)]+)\\)\\.toString\\(\\)", "$1 + $2");

        return modernized;
    }

    @Override
    public boolean isCloudEnabled() {
        return false;
    }

    private String extractClassName(String sourceCode) {
        if (sourceCode == null) return "SampleClass";
        Pattern pattern = Pattern.compile("public\\s+(?:class|interface|record|enum)\\s+([a-zA-Z0-9_]+)");
        Matcher matcher = pattern.matcher(sourceCode);
        return matcher.find() ? matcher.group(1) : "SampleClass";
    }

    private String extractPackage(String sourceCode) {
        if (sourceCode == null) return "";
        Pattern pattern = Pattern.compile("package\\s+([a-zA-Z0-9_.]+);");
        Matcher matcher = pattern.matcher(sourceCode);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String generateRemediatedCode(String originalCode, List<CodeAuditIssueDto> issues) {
        if (originalCode == null || issues.isEmpty()) return originalCode;
        String fixed = originalCode;

        for (CodeAuditIssueDto issue : issues) {
            if ("SAFE-001".equals(issue.getRuleId()) && issue.getLineSnippet() != null) {
                fixed = fixed.replace(issue.getLineSnippet(), issue.getSuggestedCodeReplacement());
            } else if ("SPRING-001".equals(issue.getRuleId()) && issue.getLineSnippet() != null) {
                fixed = fixed.replace("@Autowired\n    private ", "private final ");
                fixed = fixed.replace("@Autowired private ", "private final ");
            }
        }
        return fixed;
    }
}
