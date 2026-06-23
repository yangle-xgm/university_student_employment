package com.example.employment.assessment.service;

import com.example.employment.assessment.dto.AssessmentDTO;
import com.example.employment.assessment.dto.QuestionDTO;
import com.example.employment.assessment.dto.ReportDTO;
import com.example.employment.assessment.dto.SubmitAnswersRequest;
import com.example.employment.assessment.dto.response.SubmitResultDTO;
import com.example.employment.common.dto.response.ApiResponse;

import java.util.List;

public interface AssessmentService {
    ApiResponse<List<AssessmentDTO>> getAssessmentList();
    ApiResponse<AssessmentDTO> getAssessmentById(Long id);
    ApiResponse<List<QuestionDTO>> getQuestionsByAssessmentId(Long assessmentId);
    ApiResponse<SubmitResultDTO> submitAnswers(Long userId, SubmitAnswersRequest request);
    ApiResponse<ReportDTO> getReportById(Long reportId);
    ApiResponse<List<ReportDTO>> getReportsByStudentId(Long userId);
}