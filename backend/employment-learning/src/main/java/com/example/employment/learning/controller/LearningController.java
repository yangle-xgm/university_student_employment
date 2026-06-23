package com.example.employment.learning.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.learning.entity.LearningPath;
import com.example.employment.learning.entity.LearningRecord;
import com.example.employment.learning.entity.LearningResource;
import com.example.employment.learning.service.LearningService;
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
    public ResponseEntity<ApiResponse<List<LearningResource>>> getResources(
            @RequestParam(required = false) String category) {
        List<LearningResource> resources = learningService.getResourceList(category);
        return ResponseEntity.ok(ApiResponse.success(resources));
    }

    @GetMapping("/resources/search")
    @Operation(summary = "搜索学习资源", description = "按关键词搜索学习资源")
    public ResponseEntity<ApiResponse<List<LearningResource>>> searchResources(@RequestParam String keyword) {
        List<LearningResource> resources = learningService.searchResources(keyword);
        return ResponseEntity.ok(ApiResponse.success(resources));
    }

    @GetMapping("/resources/{resourceId}")
    @Operation(summary = "获取资源详情", description = "根据ID获取学习资源详情")
    public ResponseEntity<ApiResponse<LearningResource>> getResourceById(@PathVariable Long resourceId) {
        LearningResource resource = learningService.getResourceById(resourceId);
        return ResponseEntity.ok(ApiResponse.success(resource));
    }

    @GetMapping("/paths")
    @Operation(summary = "获取学习路径列表", description = "获取学习路径列表，支持按状态筛选")
    public ResponseEntity<ApiResponse<List<LearningPath>>> getLearningPaths(
            @RequestParam(required = false) String status) {
        List<LearningPath> paths = learningService.getLearningPathList(status);
        return ResponseEntity.ok(ApiResponse.success(paths));
    }

    @GetMapping("/paths/{pathId}")
    @Operation(summary = "获取学习路径详情", description = "根据ID获取学习路径详情")
    public ResponseEntity<ApiResponse<LearningPath>> getLearningPathById(@PathVariable Long pathId) {
        LearningPath path = learningService.getLearningPathById(pathId);
        return ResponseEntity.ok(ApiResponse.success(path));
    }

    @GetMapping("/records/{userId}")
    @Operation(summary = "获取学习记录", description = "获取指定学生的学习记录")
    public ResponseEntity<ApiResponse<List<LearningRecord>>> getLearningRecords(@PathVariable Long userId) {
        List<LearningRecord> records = learningService.getLearningRecords(userId);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @PostMapping("/records/progress")
    @Operation(summary = "更新学习进度", description = "更新学生学习资源的进度")
    public ResponseEntity<ApiResponse<LearningRecord>> updateProgress(
            @RequestParam Long userId,
            @RequestParam Long resourceId,
            @RequestParam Double progress) {
        LearningRecord record = learningService.updateLearningProgress(userId, resourceId, progress);
        return ResponseEntity.ok(ApiResponse.success(record));
    }
}