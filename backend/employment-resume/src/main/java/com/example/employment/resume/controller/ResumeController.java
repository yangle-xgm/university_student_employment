package com.example.employment.resume.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.resume.dto.request.CreateResumeRequest;
import com.example.employment.resume.dto.request.UpdateResumeRequest;
import com.example.employment.resume.dto.response.ResumeDTO;
import com.example.employment.resume.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ApiResponse<ResumeDTO>> getResumeById(@PathVariable Long resumeId) {
        ResumeDTO resumeDTO = resumeService.getResumeById(resumeId);
        return ResponseEntity.ok(ApiResponse.success(resumeDTO));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户简历列表", description = "根据用户ID获取其所有简历")
    public ResponseEntity<ApiResponse<List<ResumeDTO>>> getResumesByUserId(@PathVariable Long userId) {
        List<ResumeDTO> resumes = resumeService.getResumesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(resumes));
    }

    @PostMapping
    @Operation(summary = "创建简历", description = "创建新简历")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ResumeDTO>> createResume(@Valid @RequestBody CreateResumeRequest request) {
        ResumeDTO resumeDTO = resumeService.createResume(request);
        return ResponseEntity.ok(ApiResponse.success("创建成功", resumeDTO));
    }

    @PutMapping("/{resumeId}")
    @Operation(summary = "更新简历", description = "更新简历信息")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ResumeDTO>> updateResume(
            @PathVariable Long resumeId,
            @Valid @RequestBody UpdateResumeRequest request) {
        ResumeDTO resumeDTO = resumeService.updateResume(resumeId, request);
        return ResponseEntity.ok(ApiResponse.success("更新成功", resumeDTO));
    }

    @DeleteMapping("/{resumeId}")
    @Operation(summary = "删除简历", description = "删除简历")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long resumeId) {
        resumeService.deleteResume(resumeId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}