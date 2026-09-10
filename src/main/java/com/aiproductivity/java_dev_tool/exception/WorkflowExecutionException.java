package com.aiproductivity.java_dev_tool.exception;

public class WorkflowExecutionException extends RuntimeException {
    public WorkflowExecutionException(String message) {
        super(message);
    }

    public WorkflowExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
