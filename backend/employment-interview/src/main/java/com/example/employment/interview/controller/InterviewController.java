package com.example.employment.interview.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.interview.dto.request.CreateInterviewRequest;
import com.example.employment.interview.dto.request.UpdateInterviewRequest;
import com.example.employment.interview.dto.response.InterviewDTO;
import com.example.employment.interview.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
@Tag(name = "面试管理", description = "面试信息管理相关接口")
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping("/{interviewId}")
    @Operation(summary = "获取面试详情", description = "根据面试ID获取面试详细信息")
    public ResponseEntity<ApiResponse<InterviewDTO>> getInterviewById(@PathVariable Long interviewId) {
        InterviewDTO interviewDTO = interviewService.getInterviewById(interviewId);
        return ResponseEntity.ok(ApiResponse.success(interviewDTO));
    }

    @GetMapping
    @Operation(summary = "获取所有面试", description = "获取所有面试列表")
    public ResponseEntity<ApiResponse<List<InterviewDTO>>> getAllInterviews() {
        List<InterviewDTO> interviews = interviewService.getAllInterviews();
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户面试列表", description = "根据用户ID获取其所有面试")
    public ResponseEntity<ApiResponse<List<InterviewDTO>>> getInterviewsByUserId(@PathVariable Long userId) {
        List<InterviewDTO> interviews = interviewService.getInterviewsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "获取职位面试列表", description = "根据职位ID获取相关面试")
    public ResponseEntity<ApiResponse<List<InterviewDTO>>> getInterviewsByJobId(@PathVariable Long jobId) {
        List<InterviewDTO> interviews = interviewService.getInterviewsByJobId(jobId);
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @PostMapping
    @Operation(summary = "创建面试", description = "安排新面试")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InterviewDTO>> createInterview(@Valid @RequestBody CreateInterviewRequest request) {
        InterviewDTO interviewDTO = interviewService.createInterview(request);
        return ResponseEntity.ok(ApiResponse.success(interviewDTO));
    }

    @PutMapping("/{interviewId}")
    @Operation(summary = "更新面试", description = "更新面试信息")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InterviewDTO>> updateInterview(@PathVariable Long interviewId, @Valid @RequestBody UpdateInterviewRequest request) {
        InterviewDTO interviewDTO = interviewService.updateInterview(interviewId, request);
        return ResponseEntity.ok(ApiResponse.success(interviewDTO));
    }

    @DeleteMapping("/{interviewId}")
    @Operation(summary = "删除面试", description = "删除面试记录")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteInterview(@PathVariable Long interviewId) {
        interviewService.deleteInterview(interviewId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
