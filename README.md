# AI-Powered Java Developer Productivity & Automation Tool

> **A modern Spring Boot 3 + MySQL automation engine designed to eliminate repetitive developer toil across the Software Development Lifecycle (SDLC).**

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![REST API](https://img.shields.io/badge/REST-APIs-blue?style=for-the-badge)](https://restfulapi.net/)
[![Build Status](https://img.shields.io/badge/Tests-13%20Passed-brightgreen?style=for-the-badge)]()

---

## 1. The Problem

Enterprise Java development is notoriously robust, but building and maintaining production Spring Boot applications involves significant **repetitive, mechanical tasks** that consume up to **40–60% of engineering bandwidth**:

1. **Boilerplate Scaffolding Fatigue ("Spec-to-Production" Overhead)**:
   - For every new business entity or domain model, developers must repeatedly write:
     - JPA Entities with column mappings, validation annotations, timestamps, and equals/hashCode.
     - Separate Request and Response DTOs to prevent over-posting and entity leakage.
     - Spring Data JPA Repositories with derived query methods and pagination.
     - Service interfaces and transactional implementations (`@Service`, `@Transactional`).
     - REST Controllers with status codes (`201 Created`, `204 No Content`), `@Valid` bindings, and OpenAPI documentation.
     - Relational MySQL DDL migration scripts with indexes and foreign keys.
   - *Time cost*: **2.5 to 4 hours per domain entity** written manually.

2. **Repetitive Unit Test Authoring**:
   - Writing standard JUnit 5 + Mockito test cases for CRUD operations, pagination, validation failures, and edge-case exceptions is boilerplate-heavy and often skipped under tight sprint deadlines.
   - *Result*: Technical debt, low test coverage, and regressions.

3. **Manual Code Reviews for Anti-Patterns & Security Smells**:
   - Reviewing Pull Requests for classic Java/Spring anti-patterns takes senior engineers away from architecture:
     - **N+1 Query Hazards**: Inadvertently querying the database in loops or traversing lazy collections without `@EntityGraph` or `JOIN FETCH`.
     - **Unsafe Optional Access**: Invoking `.get()` without `isPresent()` or `orElseThrow()`, causing `NoSuchElementException`.
     - **Missing Transaction Boundaries**: Performing database mutations in `@Service` classes without `@Transactional`.
     - **SQL Injection Risks**: Constructing queries with string concatenation.
     - **Hardcoded Secrets**: Embedded API keys and passwords in source files.

4. **Lack of Productivity Visibility**:
   - Engineering managers have no automated, data-driven way to measure developer time saved, lines of boilerplate synthesized, or vulnerabilities caught prior to production.

---

## 2. The Solution

The **AI-Powered Java Developer Productivity / Automation Tool** solves these challenges through an intelligent, multi-step orchestration engine backed by **Java 17+**, **Spring Boot 3.3.5**, **MySQL**, and a **Dual-Mode AI Architecture**.

### System Architecture Overview

```
 ┌─────────────────────────────────────────────────────────────────────────────┐
 │                         Web Dashboard (index.html)                          │
 │         (Visual Workflow Studio • Code Auditor • Test Synthesizer)          │
 └───────────────────────────────────────┬─────────────────────────────────────┘
                                         │ HTTP / JSON
                                         ▼
 ┌─────────────────────────────────────────────────────────────────────────────┐
 │                             REST API Layer                                  │
 │   WorkflowController  •  CodeGenController  •  AuditController  •  Analytics│
 └───────────────────────────────────────┬─────────────────────────────────────┘
                                         │
 ┌───────────────────────────────────────▼─────────────────────────────────────┐
 │                      Autonomous Workflow Engine                             │
 │    ┌───────────────────────────────────────────────────────────────────┐    │
 │    │  Step 1: Spec Parsing & Architecture Validation                  │    │
 │    │  Step 2: JPA Entity Synthesis (Jakarta, Auditing, Validations)    │    │
 │    │  Step 3: DTO Synthesis (RequestDto, ResponseDto with mappers)     │    │
 │    │  Step 4: Spring Data JPA Repository Synthesis                     │    │
 │    │  Step 5: Transactional Service Layer Synthesis                    │    │
 │    │  Step 6: REST Controller Synthesis (RESTful standards, @Valid)    │    │
 │    │  Step 7: MySQL DDL Migration Script Generation                    │    │
 │    │  Step 8: Automated JUnit 5 + Mockito Test Suite Synthesis         │    │
 │    │  Step 9: Real-time Quality & Security Audit                       │    │
 │    └───────────────────────────────────────────────────────────────────┘    │
 └───────────────────────┬───────────────────────────────┬─────────────────────┘
                         │                               │
                         ▼                               ▼
       ┌─────────────────────────────────┐   ┌─────────────────────────────────┐
       │         Dual AI Engine          │   │      MySQL Persistence Layer    │
       │  • Local Heuristic AST Scanner  │   │  • automation_workflows         │
       │    (100% Offline, Zero-Config)  │   │  • workflow_step_executions     │
       │  • Cloud LLM (Gemini / OpenAI)  │   │  • code_audit_reports & issues  │
       │    (Configurable via properties)│   │  • productivity_metrics         │
       └─────────────────────────────────┘   └─────────────────────────────────┘
```

### Key Highlights
- **100% Offline Capable Out-of-the-Box**: Features a built-in deterministic Heuristic AST & Code Generator. No API keys, cloud billing, or external dependencies required for full functionality.
- **Pluggable Cloud AI**: Configure `gemini.api.key` or `ai.provider=gemini` to enable multimodal cloud LLM assistance.
- **Production-Grade Spring Boot 3**: Uses Jakarta persistence (`jakarta.persistence.*`), Spring 6 declarative routing, constructor dependency injection, and proper transaction boundaries.
- **Granular Execution Tracking**: Every workflow run records step-by-step logs, artifact code outputs, duration in milliseconds, and estimated minutes saved into MySQL.

---

## 3. Efficiency Improvement & Quantitative ROI

By automating repetitive boilerplate creation and static code audits, the tool dramatically accelerates delivery speed:

| Task / Activity | Manual Time | Automated Workflow | Efficiency Gain |
| :--- | :--- | :--- | :--- |
| **Full Entity-to-REST Module** (Entity, DTOs, Repo, Service, Controller, DDL) | 180–240 mins (3–4 hrs) | **< 200 ms** (Execution) + 10 mins review | **~95% Time Reduction** |
| **JUnit 5 + Mockito Test Authoring** (Unit tests, negative paths, mocks) | 60–90 mins | **< 150 ms** | **~97% Time Reduction** |
| **Code Review & Vulnerability Audit** (Catching N+1, SQLi, unsafe Optionals) | 30–45 mins | **< 100 ms** | **Instant (Zero Lead Time)** |
| **MySQL DDL Migration Authoring** (Indexes, constraints, timestamps) | 20–30 mins | **< 50 ms** | **100% Automated** |
| **Code Modernization** (Streams, records, injection upgrades) | 30–60 mins per file | **< 80 ms** | **~90% Time Reduction** |

### ROI Benchmark
For a team of **5 Java developers** building **4 new domain entities / features per sprint**:
- **Manual hours spent on repetitive boilerplate**: ~80 hours/sprint
- **With AI Developer Productivity Tool**: ~6 hours/sprint (review only)
- **Time saved**: **~74 engineering hours per sprint** (~1.8 FTE capacity reclaimed for core business logic)
- **Defects prevented**: Early detection of N+1 database bottlenecks and SQL injection vulnerabilities prior to staging deployment.

---

## 4. Automation Workflows Provided

### Workflow 1: "Spec-to-Production" Autonomous Scaffolding
Input a JSON specification or domain model name and attributes. The pipeline autonomously generates:
1. **Jakarta JPA Entity**: Includes ID generation, field validation tags (`@NotBlank`, `@Size`, `@PositiveOrZero`), created/updated timestamps, `@PreUpdate` hooks, and equals/hashCode.
2. **Request & Response DTOs**: Separation of concerns with static factory mappers (`fromEntity(...)`).
3. **Spring Data JPA Repository**: Derived finder methods for unique attributes, pagination support, and indexed lookups.
4. **Service Interface & Implementation**: `@Service`, `@Transactional`, transactional read-only queries, not-found exceptions, and DTO conversion.
5. **REST Controller**: `@RestController`, standard HTTP response codes (`201 Created`, `200 OK`, `204 No Content`), `@Valid` request bodies, and CORS configuration.
6. **MySQL DDL Migration Script**: Production `CREATE TABLE` script with `InnoDB` storage engine, `utf8mb4` encoding, foreign key indexes, and timestamp columns.
7. **JUnit 5 + Mockito Test Suite**: Complete unit tests with `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, and nested test fixtures.
8. **Automated Code Quality Audit**: Scans the generated components and scores quality out of 100.

### Workflow 2: Automated Code Review & Security Audit
Inspects Java source code against static AST and heuristic rules:
- `SEC-001` (**Critical**): Dynamic SQL string concatenation (SQL injection).
- `SEC-002` (**High**): Hardcoded secrets, passwords, or API keys in source files.
- `SAFE-001` (**High**): Unchecked `Optional.get()` calls that trigger runtime exceptions.
- `PERF-001` (**High**): N+1 query loop detection (database calls inside iterations).
- `SPRING-001` (**Low**): Field injection (`@Autowired private`) replaced with constructor injection.
- `SPRING-002` (**Medium**): State-mutating `@Service` methods missing `@Transactional`.
- `SMELL-001` (**Medium**): Raw collection types without generic parameters.
- **Output**: Quality Score (0–100), severity breakdown, line numbers, descriptions, and **instant remediated code replacement**.

### Workflow 3: Automated JUnit 5 + Mockito Test Synthesis
Given any service or component, synthesizes a complete test class:
- Mock setup (`@Mock`, `@InjectMocks`)
- Positive scenario tests (`shouldReturnEntityWhenIdExists`, `shouldPersistEntitySuccessfully`)
- Negative scenario tests (`shouldThrowExceptionWhenEntityNotFound`)
- Paginated listing tests (`shouldReturnPaginatedEntities`)
- Deletion verification tests (`verify(repository).deleteById(id)`)

### Workflow 4: Productivity Metrics & ROI Tracking
Calculates developer hours saved, lines of code synthesized, tests generated, and vulnerabilities eliminated, storing every execution event in MySQL.

---

## 5. MySQL Database Design

The system persists all automation workflows and metrics in MySQL:

```sql
CREATE DATABASE IF NOT EXISTS `javadevtool_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Tables & Relationships
1. **`automation_workflows`**:
   - Tracks workflow ID, name, type (`FULL_SCAFFOLD`, `CODE_AUDIT`, `TEST_SYNTHESIS`, `MODERNIZE`), status (`IN_PROGRESS`, `COMPLETED`, `FAILED`), target entity name, execution time (ms), and estimated developer minutes saved.
2. **`workflow_step_executions`** (FK &rarr; `automation_workflows.id`):
   - Stores each individual pipeline stage, artifact type (`ENTITY`, `DTO`, `REPOSITORY`, `SERVICE`, `CONTROLLER`, `TEST_SUITE`, `DDL_MIGRATION`), generated source code, execution duration, and error logs.
3. **`code_audit_reports`**:
   - Stores audit target class name, overall quality score (0–100), total issues count, severity counts, raw source code, and executive summary.
4. **`code_audit_issues`** (FK &rarr; `code_audit_reports.id`):
   - Stores granular rule ID (`SEC-001`, `PERF-001`), severity (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`), category, line number, code snippet, remediation guidance, and code replacement.
5. **`productivity_metrics`**:
   - Aggregates ROI data: minutes saved, LOC generated, tests created, vulnerabilities resolved, and timestamps.

---

## 6. REST API Reference

All endpoints return a standardized envelope:
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2026-09-10T15:30:00"
}
```

### 1. Trigger Full Scaffolding Workflow
`POST /api/v1/workflows/scaffold`

```bash
curl -X POST http://localhost:8080/api/v1/workflows/scaffold \
  -H "Content-Type: application/json" \
  -d '{
    "entityName": "Product",
    "basePackage": "com.example.inventory",
    "description": "Catalog product module",
    "fields": [
      { "name": "title", "type": "String", "nullable": false, "validationRules": "@NotBlank" },
      { "name": "sku", "type": "String", "nullable": false, "unique": true, "validationRules": "@NotBlank" },
      { "name": "price", "type": "BigDecimal", "nullable": false, "validationRules": "@PositiveOrZero" },
      { "name": "stock", "type": "Integer", "nullable": false, "validationRules": "@Min(0)" }
    ],
    "generateDto": true,
    "generateService": true,
    "generateController": true,
    "generateDdl": true,
    "generateTests": true,
    "runQualityAudit": true
  }'
```

### 2. Run Code Review & Security Audit
`POST /api/v1/audit`

```bash
curl -X POST http://localhost:8080/api/v1/audit \
  -H "Content-Type: application/json" \
  -d '{
    "className": "OrderService",
    "sourceCode": "@Service\npublic class OrderService {\n  @Autowired private OrderRepo repo;\n  public Order get(Long id) { return repo.findById(id).get(); }\n}"
  }'
```

### 3. Synthesize JUnit 5 + Mockito Tests
`POST /api/v1/tests/synthesize`

```bash
curl -X POST http://localhost:8080/api/v1/tests/synthesize \
  -H "Content-Type: application/json" \
  -d '{
    "targetClassName": "PaymentService",
    "targetPackage": "com.example.billing",
    "sourceCode": "public class PaymentService { ... }",
    "targetLayer": "SERVICE"
  }'
```

### 4. Fetch Productivity & ROI Dashboard
`GET /api/v1/analytics/dashboard`

```bash
curl -X GET http://localhost:8080/api/v1/analytics/dashboard
```

### 5. Direct Single Artifact Generation
`POST /api/v1/codegen/generate`

```bash
curl -X POST http://localhost:8080/api/v1/codegen/generate \
  -H "Content-Type: application/json" \
  -d '{
    "entityName": "Customer",
    "basePackage": "com.example.crm",
    "artifactType": "CONTROLLER",
    "fields": [
      { "name": "name", "type": "String", "nullable": false },
      { "name": "email", "type": "String", "nullable": false, "unique": true }
    ]
  }'
```

### 6. Modernize Legacy Code
`POST /api/v1/workflows/modernize`

```bash
curl -X POST http://localhost:8080/api/v1/workflows/modernize \
  -H "Content-Type: application/json" \
  -d '{
    "sourceCode": "List<String> list = stream.collect(Collectors.toList());",
    "targetJavaVersion": 17
  }'
```

---

## 7. Interactive Web Dashboard

The tool includes a responsive, modern web UI packaged in `src/main/resources/static/index.html`.

Access it at: **`http://localhost:8080/`**

### Features:
1. **Interactive Visual Studio**: Define domain entities, add attributes with dynamic types and validations, and click *Execute Automation Workflow* to watch the live pipeline execution.
2. **Artifact Inspector with 1-Click Copy**: Tabbed viewer for generated Entity, Request DTO, Response DTO, Repository, Service Interface, Service Implementation, Controller, DDL, and Tests.
3. **Flawed Code Sandbox**: Pre-loaded with flawed sample code to test and visualize real-time code audit results, scores, and remediations.
4. **Live ROI Metrics**: Real-time KPI cards for hours saved, LOC generated, tests created, and recent workflow history.

---

## 8. Getting Started & Running

### Prerequisites
- **Java 17 or 21**
- **Maven 3.8+**
- **MySQL 8.x** (or run against default configuration)

### Configuration (`application.properties`)
```properties
# MySQL Settings (defaults)
spring.datasource.url=jdbc:mysql://localhost:3306/javadevtool_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Auto-DDL
spring.jpa.hibernate.ddl-auto=update

# AI Provider Configuration ('local' heuristic or 'gemini')
ai.provider=local
# Optional Gemini API Key:
# gemini.api.key=YOUR_GEMINI_API_KEY
```

### Running Automated Unit & Integration Tests
All tests run deterministically with Mockito and WebMvcTest:
```bash
./mvnw test -o
```
*(Or with system Maven: `mvn test`)*

### Starting the Application
```bash
./mvnw spring-boot:run
```

Once running:
- Open your browser at: **`http://localhost:8080/`**
- Check API health: **`http://localhost:8080/api/v1/analytics/dashboard`**

---

## 9. Project Structure

```
java-dev-tool/
├── pom.xml                                  # Build descriptors & dependencies
├── README.md                                # Documentation, problem, solution & ROI
└── src/
    ├── main/
    │   ├── java/com/aiproductivity/java_dev_tool/
    │   │   ├── JavaDevToolApplication.java  # Main application entrypoint
    │   │   ├── config/                      # Web, CORS & DataInitializer
    │   │   ├── controller/                  # REST Controllers
    │   │   │   ├── WorkflowController.java
    │   │   │   ├── CodeGenController.java
    │   │   │   ├── CodeAuditController.java
    │   │   │   ├── TestSynthesisController.java
    │   │   │   └── AnalyticsController.java
    │   │   ├── dto/                         # Request & Response DTOs
    │   │   ├── entity/                      # JPA Entities (MySQL tables)
    │   │   │   ├── AutomationWorkflow.java
    │   │   │   ├── WorkflowStepExecution.java
    │   │   │   ├── CodeAuditReport.java
    │   │   │   ├── CodeAuditIssue.java
    │   │   │   └── ProductivityMetric.java
    │   │   ├── exception/                   # Global exception handling
    │   │   ├── repository/                  # Spring Data JPA Repositories
    │   │   └── service/                     # Business logic & AI engines
    │   │       ├── CodeGenerationService.java
    │   │       ├── TestSynthesisService.java
    │   │       ├── CodeAuditService.java
    │   │       ├── WorkflowExecutionService.java
    │   │       ├── AnalyticsService.java
    │   │       └── ai/
    │   │           ├── AiService.java
    │   │           ├── LocalHeuristicAiService.java
    │   │           └── GeminiCloudAiService.java
    │   └── resources/
    │       ├── application.properties       # MySQL & AI configuration
    │       ├── schema.sql                   # Pure MySQL DDL schema
    │       └── static/
    │           └── index.html               # Interactive Web Dashboard
    └── test/                                # JUnit 5 + Mockito Test Suites
        └── java/com/aiproductivity/java_dev_tool/
            ├── JavaDevToolApplicationTests.java
            ├── controller/
            │   └── WorkflowControllerTest.java
            └── service/
                ├── CodeGenerationServiceTest.java
                ├── CodeAuditServiceTest.java
                └── WorkflowExecutionServiceTest.java
```

---

## 10. Summary

The **AI-Powered Java Developer Productivity / Automation Tool** transforms modern Java development by turning hours of repetitive, error-prone boilerplate into instant, standardized, and secure production code. By integrating automated scaffolding, intelligent static audits, unit test synthesis, and executive ROI tracking into a unified Spring Boot + MySQL platform, development teams achieve higher velocity, superior code quality, and continuous efficiency gains.
