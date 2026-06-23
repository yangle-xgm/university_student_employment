package com.example.employment.assessment.controller;

import com.example.employment.assessment.dto.AssessmentDTO;
import com.example.employment.assessment.dto.QuestionDTO;
import com.example.employment.assessment.dto.ReportDTO;
import com.example.employment.assessment.dto.SubmitAnswersRequest;
import com.example.employment.assessment.dto.response.SubmitResultDTO;
import com.example.employment.assessment.service.AssessmentService;
import com.example.employment.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
@Tag(name = "职业测评", description = "职业兴趣、性格特质、能力倾向测评管理")
public class AssessmentController {

    private final AssessmentService assessmentService;

    @GetMapping
    @Operation(summary = "获取测评列表", description = "获取所有可用的职业测评")
    public ResponseEntity<ApiResponse<List<AssessmentDTO>>> getAssessmentList() {
        return ResponseEntity.ok(assessmentService.getAssessmentList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取测评详情", description = "根据ID获取测评详情")
    public ResponseEntity<ApiResponse<AssessmentDTO>> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    @GetMapping("/{id}/questions")
    @Operation(summary = "获取测评题目", description = "获取指定测评的所有题目")
    public ResponseEntity<ApiResponse<List<QuestionDTO>>> getQuestions(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getQuestionsByAssessmentId(id));
    }

    @PostMapping("/submit")
    @Operation(summary = "提交测评答案", description = "提交测评答案并生成报告")
    public ResponseEntity<ApiResponse<SubmitResultDTO>> submitAnswers(
            @RequestParam Long userId,
            @RequestBody SubmitAnswersRequest request) {
        return ResponseEntity.ok(assessmentService.submitAnswers(userId, request));
    }

    @GetMapping("/reports/{reportId}")
    @Operation(summary = "获取测评报告", description = "根据报告ID获取测评报告")
    public ResponseEntity<ApiResponse<ReportDTO>> getReportById(@PathVariable Long reportId) {
        return ResponseEntity.ok(assessmentService.getReportById(reportId));
    }

    @GetMapping("/reports/student/{userId}")
    @Operation(summary = "获取学生测评报告列表", description = "获取指定学生的所有测评报告")
    public ResponseEntity<ApiResponse<List<ReportDTO>>> getReportsByStudentId(@PathVariable Long userId) {
        return ResponseEntity.ok(assessmentService.getReportsByStudentId(userId));
    }
}