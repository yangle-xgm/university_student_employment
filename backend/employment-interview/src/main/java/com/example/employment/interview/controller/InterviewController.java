package com.example.employment.interview.controller;

import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.interview.entity.Interview;
import com.example.employment.interview.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Interview>> getInterviewById(@PathVariable Long interviewId) {
        Interview interview = interviewService.getInterviewById(interviewId);
        return ResponseEntity.ok(ApiResponse.success(interview));
    }

    @GetMapping
    @Operation(summary = "获取所有面试", description = "获取所有面试列表")
    public ResponseEntity<ApiResponse<List<Interview>>> getAllInterviews() {
        List<Interview> interviews = interviewService.getAllInterviews();
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取学生面试列表", description = "根据学生ID获取面试列表")
    public ResponseEntity<ApiResponse<List<Interview>>> getInterviewsByUserId(@PathVariable Long userId) {
        List<Interview> interviews = interviewService.getInterviewsByStudentId(userId);
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @GetMapping("/job/{jobId}")
    @Operation(summary = "获取职位面试列表", description = "根据职位ID获取面试列表")
    public ResponseEntity<ApiResponse<List<Interview>>> getInterviewsByJobId(@PathVariable Long jobId) {
        List<Interview> interviews = interviewService.getInterviewsByJobId(jobId);
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @GetMapping("/application/{applicationId}")
    @Operation(summary = "获取申请相关面试列表", description = "根据申请ID获取相关面试")
    public ResponseEntity<ApiResponse<List<Interview>>> getInterviewsByApplicationId(@PathVariable Long applicationId) {
        List<Interview> interviews = interviewService.getInterviewsByApplicationId(applicationId);
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @PostMapping
    @Operation(summary = "创建面试", description = "安排新面试")
    public ResponseEntity<ApiResponse<Interview>> createInterview(@RequestBody Interview interview) {
        Interview created = interviewService.createInterview(interview);
        return ResponseEntity.ok(ApiResponse.success("创建成功", created));
    }

    @PutMapping("/{interviewId}")
    @Operation(summary = "更新面试", description = "更新面试信息")
    public ResponseEntity<ApiResponse<Interview>> updateInterview(
            @PathVariable Long interviewId,
            @RequestBody Interview interview) {
        Interview updated = interviewService.updateInterview(interviewId, interview);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
    }

    @DeleteMapping("/{interviewId}")
    @Operation(summary = "删除面试", description = "删除面试记录")
    public ResponseEntity<ApiResponse<Void>> deleteInterview(@PathVariable Long interviewId) {
        interviewService.deleteInterview(interviewId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
}