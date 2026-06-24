package com.example.employment.interview.controller;

import com.example.employment.common.util.SecurityUtils;
import com.example.employment.interview.entity.Interview;
import com.example.employment.interview.entity.InterviewEvaluation;
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
@Tag(name = "面试管理", description = "面试安排、评估、记录管理")
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping
    @Operation(summary = "获取面试列表", description = "按申请ID筛选面试列表")
    public ResponseEntity<List<Interview>> listAll(@RequestParam(required = false) Long applicationId) {
        return ResponseEntity.ok(interviewService.listAll(applicationId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "获取学生面试列表", description = "按学生档案ID获取所有面试")
    public ResponseEntity<List<Interview>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(interviewService.getByStudentId(studentId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取面试详情", description = "根据ID获取面试详情")
    public ResponseEntity<Interview> getById(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getById(id));
    }

    @PostMapping
    @Operation(summary = "创建面试", description = "安排新的面试")
    public ResponseEntity<Interview> createInterview(@RequestBody Interview interview) {
        return ResponseEntity.ok(interviewService.createInterview(interview));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新面试", description = "更新面试信息")
    public ResponseEntity<Interview> updateInterview(@PathVariable Long id, @RequestBody Interview interview) {
        return ResponseEntity.ok(interviewService.updateInterview(id, interview));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除面试", description = "删除面试记录")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }

    // ========== 面试评估 ==========

    @PostMapping("/{id}/evaluations")
    @Operation(summary = "添加面试评估", description = "为面试添加评估记录")
    public ResponseEntity<InterviewEvaluation> addEvaluation(
            @PathVariable Long id,
            @RequestParam(required = false) Long evaluatorId,
            @RequestBody InterviewEvaluation evaluation) {
        Long effectiveEvaluatorId = evaluatorId != null ? evaluatorId : SecurityUtils.getCurrentUserId();
        if (effectiveEvaluatorId == null) {
            throw new IllegalArgumentException("无法获取评估人ID");
        }
        return ResponseEntity.ok(interviewService.addEvaluation(id, effectiveEvaluatorId, evaluation));
    }

    @GetMapping("/{id}/evaluations")
    @Operation(summary = "获取面试评估列表", description = "获取指定面试的所有评估记录")
    public ResponseEntity<List<InterviewEvaluation>> getEvaluations(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getEvaluationsByInterviewId(id));
    }

    @DeleteMapping("/evaluations/{evaluationId}")
    @Operation(summary = "删除面试评估", description = "删除指定的面试评估记录")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long evaluationId) {
        interviewService.deleteEvaluation(evaluationId);
        return ResponseEntity.noContent().build();
    }
}