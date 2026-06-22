package com.example.employment.resume.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.resume.entity.Resume;
import com.example.employment.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@Tag(name = "简历管理", description = "简历信息管理相关接口")
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/{resumeId}")
    @Operation(summary = "获取简历详情", description = "根据简历ID获取简历详细信息")
    public ResponseEntity<ApiResponse<Resume>> getResumeById(@PathVariable Long resumeId) {
        Resume resume = resumeService.getResumeById(resumeId);
        return ResponseEntity.ok(ApiResponse.success(resume));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "获取学生简历列表", description = "根据学生ID获取其所有简历")
    public ResponseEntity<ApiResponse<List<Resume>>> getResumesByStudentId(@PathVariable Long studentId) {
        List<Resume> resumes = resumeService.getResumesByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success(resumes));
    }

    @PostMapping
    @Operation(summary = "创建简历", description = "创建新简历")
    public ResponseEntity<ApiResponse<Resume>> createResume(@RequestBody Resume resume) {
        Resume created = resumeService.createResume(resume);
        return ResponseEntity.ok(ApiResponse.success("创建成功", created));
    }

    @PutMapping("/{resumeId}")
    @Operation(summary = "更新简历", description = "更新简历信息")
    public ResponseEntity<ApiResponse<Resume>> updateResume(
            @PathVariable Long resumeId,
            @RequestBody Resume resume) {
        Resume updated = resumeService.updateResume(resumeId, resume);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
    }

    @DeleteMapping("/{resumeId}")
    @Operation(summary = "删除简历", description = "删除简历")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long resumeId) {
        resumeService.deleteResume(resumeId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}