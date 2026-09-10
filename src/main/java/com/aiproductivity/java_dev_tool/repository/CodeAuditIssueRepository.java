package com.aiproductivity.java_dev_tool.repository;

import com.aiproductivity.java_dev_tool.entity.CodeAuditIssue;
import com.aiproductivity.java_dev_tool.entity.IssueCategory;
import com.aiproductivity.java_dev_tool.entity.IssueSeverity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeAuditIssueRepository extends JpaRepository<CodeAuditIssue, Long> {

    List<CodeAuditIssue> findByReportId(Long reportId);

    List<CodeAuditIssue> findBySeverity(IssueSeverity severity);

    List<CodeAuditIssue> findByCategory(IssueCategory category);
}
