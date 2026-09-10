package com.aiproductivity.java_dev_tool.controller;

import com.aiproductivity.java_dev_tool.dto.response.ApiResponse;
import com.aiproductivity.java_dev_tool.dto.response.DashboardMetricsResponse;
import com.aiproductivity.java_dev_tool.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardMetricsResponse>> getDashboardMetrics() {
        DashboardMetricsResponse metrics = analyticsService.getDashboardMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics));
    }
}
