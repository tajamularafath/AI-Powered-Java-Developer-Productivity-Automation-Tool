package com.aiproductivity.java_dev_tool.repository;

import com.aiproductivity.java_dev_tool.entity.CodeAuditReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeAuditReportRepository extends JpaRepository<CodeAuditReport, Long> {

    Page<CodeAuditReport> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT AVG(r.overallScore) FROM CodeAuditReport r")
    Double getAverageAuditScore();

    @Query("SELECT SUM(r.totalIssues) FROM CodeAuditReport r")
    Long getTotalIssuesIdentified();

    @Query("SELECT SUM(r.criticalCount + r.highCount) FROM CodeAuditReport r")
    Long getTotalCriticalAndHighVulnerabilities();
}
