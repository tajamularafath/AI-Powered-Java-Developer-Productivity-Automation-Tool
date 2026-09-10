package com.aiproductivity.java_dev_tool.repository;

import com.aiproductivity.java_dev_tool.entity.ArtifactType;
import com.aiproductivity.java_dev_tool.entity.WorkflowStepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowStepExecutionRepository extends JpaRepository<WorkflowStepExecution, Long> {

    List<WorkflowStepExecution> findByWorkflowIdOrderByStepOrderAsc(Long workflowId);

    List<WorkflowStepExecution> findByArtifactType(ArtifactType artifactType);
}
