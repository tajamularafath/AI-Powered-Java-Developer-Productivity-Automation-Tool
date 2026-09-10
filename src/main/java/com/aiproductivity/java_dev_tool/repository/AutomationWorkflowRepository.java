package com.aiproductivity.java_dev_tool.repository;

import com.aiproductivity.java_dev_tool.entity.AutomationWorkflow;
import com.aiproductivity.java_dev_tool.entity.WorkflowStatus;
import com.aiproductivity.java_dev_tool.entity.WorkflowType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomationWorkflowRepository extends JpaRepository<AutomationWorkflow, Long> {

    List<AutomationWorkflow> findByStatus(WorkflowStatus status);

    List<AutomationWorkflow> findByWorkflowType(WorkflowType workflowType);

    Page<AutomationWorkflow> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT SUM(w.estimatedMinutesSaved) FROM AutomationWorkflow w WHERE w.status = 'COMPLETED'")
    Double getTotalMinutesSaved();

    @Query("SELECT COUNT(w) FROM AutomationWorkflow w WHERE w.status = 'COMPLETED'")
    Long countCompletedWorkflows();
}
