package com.aiproductivity.java_dev_tool.repository;

import com.aiproductivity.java_dev_tool.entity.ProductivityMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductivityMetricRepository extends JpaRepository<ProductivityMetric, Long> {

    @Query("SELECT SUM(p.minutesSaved) FROM ProductivityMetric p")
    Double getTotalMinutesSaved();

    @Query("SELECT SUM(p.linesOfCodeGenerated) FROM ProductivityMetric p")
    Long getTotalLinesOfCodeGenerated();

    @Query("SELECT SUM(p.testsSynthesized) FROM ProductivityMetric p")
    Long getTotalTestsSynthesized();

    @Query("SELECT SUM(p.vulnerabilitiesPrevented) FROM ProductivityMetric p")
    Long getTotalVulnerabilitiesPrevented();

    List<ProductivityMetric> findTop20ByOrderByRecordedAtDesc();
}
