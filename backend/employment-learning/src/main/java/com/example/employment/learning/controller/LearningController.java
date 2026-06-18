package com.example.employment.learning.controller;

import com.example.employment.learning.dto.LearningPathDTO;
import com.example.employment.learning.dto.LearningRecordDTO;
import com.example.employment.learning.dto.ResourceDTO;
import com.example.employment.learning.service.LearningService;
import com.example.employment.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
@Tag(name = "学习成长", description = "学习资源库、学习路径、学习进度跟踪")
public class LearningController {

    private final LearningService learningService;

    @GetMapping("/resources")
    @Operation(summary = "获取学习资源列表", description = "获取学习资源列表，支持按分类筛选")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> getResources(
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(learningService.getResourceList(category));
    }

    @GetMapping("/resources/search")
    @Operation(summary = "搜索学习资源", description = "按关键词搜索学习资源")
    public ResponseEntity<ApiResponse<List<ResourceDTO>>> searchResources(@RequestParam String keyword) {
        return ResponseEntity.ok(learningService.searchResources(keyword));
    }

    @GetMapping("/resources/{resourceId}")
    @Operation(summary = "获取资源详情", description = "根据ID获取学习资源详情")
    public ResponseEntity<ApiResponse<ResourceDTO>> getResourceById(@PathVariable Long resourceId) {
        return ResponseEntity.ok(learningService.getResourceById(resourceId));
    }

    @GetMapping("/paths")
    @Operation(summary = "获取学习路径列表", description = "获取学习路径列表，支持按职业目标筛选")
    public ResponseEntity<ApiResponse<List<LearningPathDTO>>> getLearningPaths(
            @RequestParam(required = false) String careerGoal) {
        return ResponseEntity.ok(learningService.getLearningPathList(careerGoal));
    }

    @GetMapping("/paths/{pathId}")
    @Operation(summary = "获取学习路径详情", description = "根据ID获取学习路径详情")
    public ResponseEntity<ApiResponse<LearningPathDTO>> getLearningPathById(@PathVariable Long pathId) {
        return ResponseEntity.ok(learningService.getLearningPathById(pathId));
    }

    @GetMapping("/records/{studentId}")
    @Operation(summary = "获取学习记录", description = "获取指定学生的学习记录")
    public ResponseEntity<ApiResponse<List<LearningRecordDTO>>> getLearningRecords(@PathVariable Long studentId) {
        return ResponseEntity.ok(learningService.getLearningRecords(studentId));
    }

    @PostMapping("/records/progress")
    @Operation(summary = "更新学习进度", description = "更新学生学习资源的进度")
    public ResponseEntity<ApiResponse<LearningRecordDTO>> updateProgress(
            @RequestParam Long studentId,
            @RequestParam Long resourceId,
            @RequestParam Double progress) {
        return ResponseEntity.ok(learningService.updateLearningProgress(studentId, resourceId, progress));
    }
}