package com.example.employment.career.controller;

import com.example.employment.career.dto.CareerMilestoneDTO;
import com.example.employment.career.dto.CareerPlanDTO;
import com.example.employment.career.dto.CreateCareerPlanRequest;
import com.example.employment.career.dto.CreateMilestoneRequest;
import com.example.employment.career.service.CareerPlanService;
import com.example.employment.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/career-plans")
@RequiredArgsConstructor
@Tag(name = "职业规划", description = "职业目标设定、规划建议、进度跟踪")
public class CareerController {

    private final CareerPlanService careerPlanService;

    @GetMapping("/student/{studentId}")
    @Operation(summary = "获取学生职业规划列表", description = "获取指定学生的所有职业规划")
    public ResponseEntity<ApiResponse<List<CareerPlanDTO>>> getPlansByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(careerPlanService.getPlansByStudentId(studentId));
    }

    @GetMapping("/{planId}")
    @Operation(summary = "获取职业规划详情", description = "根据ID获取职业规划详情")
    public ResponseEntity<ApiResponse<CareerPlanDTO>> getPlanById(@PathVariable Long planId) {
        return ResponseEntity.ok(careerPlanService.getPlanById(planId));
    }

    @PostMapping
    @Operation(summary = "创建职业规划", description = "创建新的职业规划")
    public ResponseEntity<ApiResponse<CareerPlanDTO>> createPlan(
            @RequestParam Long studentId,
            @RequestBody CreateCareerPlanRequest request) {
        return ResponseEntity.ok(careerPlanService.createPlan(studentId, request));
    }

    @PutMapping("/{planId}")
    @Operation(summary = "更新职业规划", description = "更新职业规划信息")
    public ResponseEntity<ApiResponse<CareerPlanDTO>> updatePlan(
            @PathVariable Long planId,
            @RequestBody CreateCareerPlanRequest request) {
        return ResponseEntity.ok(careerPlanService.updatePlan(planId, request));
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "删除职业规划", description = "删除职业规划及其关联的里程碑")
    public ResponseEntity<ApiResponse<String>> deletePlan(@PathVariable Long planId) {
        return ResponseEntity.ok(careerPlanService.deletePlan(planId));
    }

    @GetMapping("/{planId}/milestones")
    @Operation(summary = "获取里程碑列表", description = "获取指定职业规划的所有里程碑")
    public ResponseEntity<ApiResponse<List<CareerMilestoneDTO>>> getMilestones(@PathVariable Long planId) {
        return ResponseEntity.ok(careerPlanService.getMilestonesByPlanId(planId));
    }

    @PostMapping("/{planId}/milestones")
    @Operation(summary = "添加里程碑", description = "为职业规划添加新里程碑")
    public ResponseEntity<ApiResponse<CareerMilestoneDTO>> addMilestone(
            @PathVariable Long planId,
            @RequestBody CreateMilestoneRequest request) {
        return ResponseEntity.ok(careerPlanService.addMilestone(planId, request));
    }

    @PutMapping("/milestones/{milestoneId}")
    @Operation(summary = "更新里程碑", description = "更新里程碑信息")
    public ResponseEntity<ApiResponse<CareerMilestoneDTO>> updateMilestone(
            @PathVariable Long milestoneId,
            @RequestBody CreateMilestoneRequest request) {
        return ResponseEntity.ok(careerPlanService.updateMilestone(milestoneId, request));
    }

    @DeleteMapping("/milestones/{milestoneId}")
    @Operation(summary = "删除里程碑", description = "删除指定里程碑")
    public ResponseEntity<ApiResponse<String>> deleteMilestone(@PathVariable Long milestoneId) {
        return ResponseEntity.ok(careerPlanService.deleteMilestone(milestoneId));
    }

    @PutMapping("/milestones/{milestoneId}/progress")
    @Operation(summary = "更新里程碑进度", description = "更新里程碑完成进度")
    public ResponseEntity<ApiResponse<CareerMilestoneDTO>> updateMilestoneProgress(
            @PathVariable Long milestoneId,
            @RequestParam Double progress) {
        return ResponseEntity.ok(careerPlanService.updateMilestoneProgress(milestoneId, progress));
    }
}