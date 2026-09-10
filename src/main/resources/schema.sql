-- ==============================================================================
-- AI-Powered Java Developer Productivity & Automation Tool
-- Database Schema for MySQL 8.x
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS `javadevtool_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `javadevtool_db`;

-- 1. Automation Workflows Table
CREATE TABLE IF NOT EXISTS `automation_workflows` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `workflow_name` VARCHAR(255) NOT NULL,
    `workflow_type` VARCHAR(50) NOT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    `target_entity_name` VARCHAR(255) NULL,
    `target_package` VARCHAR(255) NULL,
    `input_payload` LONGTEXT NULL,
    `output_summary` TEXT NULL,
    `total_execution_time_ms` BIGINT DEFAULT 0,
    `estimated_minutes_saved` DOUBLE DEFAULT 0.0,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `completed_at` DATETIME NULL,
    PRIMARY KEY (`id`),
    INDEX `idx_workflow_status` (`status`),
    INDEX `idx_workflow_type` (`workflow_type`),
    INDEX `idx_workflow_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Workflow Step Executions Table
CREATE TABLE IF NOT EXISTS `workflow_step_executions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `workflow_id` BIGINT NOT NULL,
    `step_name` VARCHAR(255) NOT NULL,
    `step_order` INT NOT NULL,
    `artifact_type` VARCHAR(50) NOT NULL,
    `artifact_name` VARCHAR(255) NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    `generated_content` LONGTEXT NULL,
    `execution_duration_ms` BIGINT DEFAULT 0,
    `error_message` TEXT NULL,
    `executed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_workflow_steps_workflow` FOREIGN KEY (`workflow_id`)
        REFERENCES `automation_workflows` (`id`) ON DELETE CASCADE,
    INDEX `idx_step_workflow_id` (`workflow_id`),
    INDEX `idx_step_artifact_type` (`artifact_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Code Audit Reports Table
CREATE TABLE IF NOT EXISTS `code_audit_reports` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `target_class_name` VARCHAR(255) NOT NULL,
    `overall_score` INT NOT NULL DEFAULT 100,
    `total_issues` INT NOT NULL DEFAULT 0,
    `critical_count` INT NOT NULL DEFAULT 0,
    `high_count` INT NOT NULL DEFAULT 0,
    `medium_count` INT NOT NULL DEFAULT 0,
    `low_count` INT NOT NULL DEFAULT 0,
    `info_count` INT NOT NULL DEFAULT 0,
    `raw_source_code` LONGTEXT NULL,
    `executive_summary` TEXT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_report_score` (`overall_score`),
    INDEX `idx_report_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Code Audit Issues Table
CREATE TABLE IF NOT EXISTS `code_audit_issues` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `report_id` BIGINT NOT NULL,
    `rule_id` VARCHAR(50) NOT NULL,
    `rule_name` VARCHAR(255) NOT NULL,
    `severity` VARCHAR(50) NOT NULL,
    `category` VARCHAR(50) NOT NULL,
    `line_number` INT NULL,
    `line_snippet` VARCHAR(500) NULL,
    `issue_description` TEXT NULL,
    `remediation_guidance` TEXT NULL,
    `suggested_code_replacement` TEXT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_audit_issues_report` FOREIGN KEY (`report_id`)
        REFERENCES `code_audit_reports` (`id`) ON DELETE CASCADE,
    INDEX `idx_issue_severity` (`severity`),
    INDEX `idx_issue_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Productivity Metrics Table
CREATE TABLE IF NOT EXISTS `productivity_metrics` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `workflow_type` VARCHAR(50) NOT NULL,
    `minutes_saved` DOUBLE NOT NULL DEFAULT 0.0,
    `lines_of_code_generated` INT NOT NULL DEFAULT 0,
    `tests_synthesized` INT NOT NULL DEFAULT 0,
    `vulnerabilities_prevented` INT NOT NULL DEFAULT 0,
    `recorded_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `idx_metric_workflow_type` (`workflow_type`),
    INDEX `idx_metric_recorded_at` (`recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
