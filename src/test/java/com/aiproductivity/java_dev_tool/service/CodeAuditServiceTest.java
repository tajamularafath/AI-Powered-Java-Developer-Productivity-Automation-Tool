package com.aiproductivity.java_dev_tool.service;

import com.aiproductivity.java_dev_tool.dto.request.CodeAuditRequest;
import com.aiproductivity.java_dev_tool.dto.response.CodeAuditResponse;
import com.aiproductivity.java_dev_tool.entity.CodeAuditReport;
import com.aiproductivity.java_dev_tool.entity.IssueSeverity;
import com.aiproductivity.java_dev_tool.repository.CodeAuditIssueRepository;
import com.aiproductivity.java_dev_tool.repository.CodeAuditReportRepository;
import com.aiproductivity.java_dev_tool.repository.ProductivityMetricRepository;
import com.aiproductivity.java_dev_tool.service.ai.GeminiCloudAiService;
import com.aiproductivity.java_dev_tool.service.ai.LocalHeuristicAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeAuditServiceTest {

    @Mock
    private CodeAuditReportRepository reportRepository;

    @Mock
    private CodeAuditIssueRepository issueRepository;

    @Mock
    private ProductivityMetricRepository metricRepository;

    private CodeAuditService codeAuditService;

    @BeforeEach
    void setUp() {
        LocalHeuristicAiService localAi = new LocalHeuristicAiService();
        GeminiCloudAiService geminiAi = new GeminiCloudAiService(localAi);
        codeAuditService = new CodeAuditService(geminiAi, reportRepository, issueRepository, metricRepository);
    }

    @Test
    @DisplayName("Should detect SQL Injection and Unsafe Optional.get() in code snippet")
    void testAuditDetectsVulnerabilities() {
        String flawedCode = """
                @Service
                public class FlawedService {
                    @Autowired
                    private OrderRepository orderRepository;

                    public Order getOrder(Long id) {
                        return orderRepository.findById(id).get();
                    }

                    public List search(String name) {
                        String sql = "SELECT * FROM orders WHERE name = '" + name;
                        return orderRepository.run(sql);
                    }
                }
                """;

        when(reportRepository.save(any(CodeAuditReport.class))).thenAnswer(inv -> {
            CodeAuditReport r = inv.getArgument(0);
            r.setId(101L);
            return r;
        });

        CodeAuditRequest request = new CodeAuditRequest("FlawedService", flawedCode);
        CodeAuditResponse response = codeAuditService.auditAndPersist(request);

        assertThat(response).isNotNull();
        assertThat(response.getReportId()).isEqualTo(101L);
        assertThat(response.getOverallScore()).isLessThan(100);
        assertThat(response.getTotalIssues()).isGreaterThanOrEqualTo(2);
        assertThat(response.getCriticalCount()).isGreaterThanOrEqualTo(1);

        // Verify issues contain SQL injection and unsafe optional
        boolean hasSqlInjection = response.getIssues().stream()
                .anyMatch(i -> i.getSeverity() == IssueSeverity.CRITICAL && i.getRuleId().equals("SEC-001"));
        boolean hasUnsafeOptional = response.getIssues().stream()
                .anyMatch(i -> i.getRuleId().equals("SAFE-001"));

        assertThat(hasSqlInjection).isTrue();
        assertThat(hasUnsafeOptional).isTrue();

        verify(reportRepository, times(1)).save(any(CodeAuditReport.class));
        verify(metricRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should grant perfect score for clean code")
    void testAuditCleanCode() {
        String cleanCode = """
                @Service
                @Transactional(readOnly = true)
                public class CleanService {
                    private final OrderRepository orderRepository;

                    public CleanService(OrderRepository orderRepository) {
                        this.orderRepository = orderRepository;
                    }

                    public Order getOrder(Long id) {
                        return orderRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Order not found"));
                    }
                }
                """;

        when(reportRepository.save(any(CodeAuditReport.class))).thenAnswer(inv -> {
            CodeAuditReport r = inv.getArgument(0);
            r.setId(102L);
            return r;
        });

        CodeAuditRequest request = new CodeAuditRequest("CleanService", cleanCode);
        CodeAuditResponse response = codeAuditService.auditAndPersist(request);

        assertThat(response.getOverallScore()).isEqualTo(100);
        assertThat(response.getTotalIssues()).isEqualTo(0);
    }
}
