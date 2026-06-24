package com.example.employment.job.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.entity.Application;
import com.example.employment.common.util.SecurityUtils;
import com.example.employment.job.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "职位申请", description = "职位申请相关接口")
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/student/{userId}")
    @Operation(summary = "获取学生申请列表", description = "根据学生ID获取申请列表")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByStudentId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByStudentId(userId)));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "获取职位申请列表", description = "根据职位ID获取申请列表")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(ApiResponse.success(applicationService.getApplicationsByJobId(jobId)));
    }

    @PostMapping("/apply")
    @Operation(summary = "申请职位", description = "学生申请职位")
    public ResponseEntity<ApiResponse<Application>> apply(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long studentId,
            @RequestParam Long jobId,
            @RequestParam(required = false) Long resumeId) {
        Long effectiveUserId = userId != null ? userId : studentId;
        if (effectiveUserId == null) {
            effectiveUserId = SecurityUtils.getCurrentUserId();
        }
        if (effectiveUserId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("无法获取当前用户ID"));
        }
        Application application = applicationService.apply(effectiveUserId, jobId, resumeId);
        return ResponseEntity.ok(ApiResponse.success("申请成功", application));
    }

    @PutMapping("/{applicationId}/status")
    @Operation(summary = "更新申请状态", description = "更新申请状态（PENDING/INTERVIEW/ACCEPTED/REJECTED）")
    public ResponseEntity<ApiResponse<Application>> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success(applicationService.updateApplicationStatus(applicationId, status)));
    }
}