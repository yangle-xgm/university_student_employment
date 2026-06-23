package com.example.employment.assessment.service.impl;

import com.example.employment.assessment.dto.AssessmentDTO;
import com.example.employment.assessment.dto.QuestionDTO;
import com.example.employment.assessment.dto.ReportDTO;
import com.example.employment.assessment.dto.SubmitAnswersRequest;
import com.example.employment.assessment.dto.response.SubmitResultDTO;
import com.example.employment.assessment.entity.Assessment;
import com.example.employment.assessment.entity.AssessmentQuestion;
import com.example.employment.assessment.entity.AssessmentRecord;
import com.example.employment.assessment.entity.AssessmentReport;
import com.example.employment.assessment.mapper.AssessmentMapper;
import com.example.employment.assessment.mapper.AssessmentQuestionMapper;
import com.example.employment.assessment.mapper.AssessmentRecordMapper;
import com.example.employment.assessment.mapper.AssessmentReportMapper;
import com.example.employment.assessment.service.AssessmentService;
import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.service.StudentProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentMapper assessmentMapper;
    private final AssessmentQuestionMapper questionMapper;
    private final AssessmentRecordMapper recordMapper;
    private final AssessmentReportMapper reportMapper;
    private final ObjectMapper objectMapper;
    private final StudentProfileService studentProfileService;

    @Override
    public ApiResponse<List<AssessmentDTO>> getAssessmentList() {
        List<Assessment> assessments = assessmentMapper.findByStatus("ACTIVE");
        List<AssessmentDTO> dtoList = assessments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    public ApiResponse<AssessmentDTO> getAssessmentById(Long id) {
        Assessment assessment = assessmentMapper.selectById(id);
        if (assessment == null) {
            throw new BusinessException("测评不存在");
        }
        return ApiResponse.success(convertToDTO(assessment));
    }

    @Override
    public ApiResponse<List<QuestionDTO>> getQuestionsByAssessmentId(Long assessmentId) {
        Assessment assessment = assessmentMapper.selectById(assessmentId);
        if (assessment == null) {
            throw new BusinessException("测评不存在");
        }
        List<AssessmentQuestion> questions = questionMapper.findByAssessmentId(assessmentId);
        List<QuestionDTO> dtoList = questions.stream()
                .map(this::convertToQuestionDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    @Transactional
    public ApiResponse<SubmitResultDTO> submitAnswers(Long studentId, SubmitAnswersRequest request) {
        Long studentProfileId = ensureStudentProfileForUser(studentId);

        Assessment assessment = assessmentMapper.selectById(request.getAssessmentId());
        if (assessment == null) {
            throw new BusinessException("测评不存在");
        }

        List<AssessmentQuestion> questions = questionMapper.findByAssessmentId(request.getAssessmentId());
        if (questions.isEmpty()) {
            throw new BusinessException("该测评暂无题目");
        }

        double totalScore = 0;
        double totalWeight = 0;
        for (SubmitAnswersRequest.AnswerDTO answer : request.getAnswers()) {
            AssessmentQuestion question = questionMapper.selectById(answer.getQuestionId());
            if (question != null) {
                double weight = question.getWeight() != null ? question.getWeight() : 1.0;
                int answerValue = parseAnswerValue(answer.getAnswer());
                totalScore += answerValue * weight;
                totalWeight += weight;
            }
        }

        double finalScore = totalWeight > 0 ? totalScore / totalWeight * 100 : 0;

        String answersJson;
        try {
            answersJson = objectMapper.writeValueAsString(request.getAnswers());
        } catch (JsonProcessingException e) {
            throw new BusinessException("答案序列化失败");
        }

        AssessmentRecord record = AssessmentRecord.builder()
                .studentId(studentProfileId)
                .assessmentId(request.getAssessmentId())
                .answers(answersJson)
                .score(finalScore)
                .status("COMPLETED")
                .completedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        recordMapper.insert(record);

        Long reportId = generateReport(record.getId(), studentProfileId, finalScore, assessment.getDimension());

        SubmitResultDTO result = SubmitResultDTO.builder()
                .reportId(reportId)
                .score(finalScore)
                .message("测评完成，得分: " + String.format("%.2f", finalScore))
                .build();

        return ApiResponse.success(result);
    }

    private Long ensureStudentProfileForUser(Long userId) {
        if (userId == null) return null;
        return studentProfileService.getOrCreateStudentProfileId(userId, true);
    }

    @Override
    public ApiResponse<ReportDTO> getReportById(Long reportId) {
        AssessmentReport report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new BusinessException("测评报告不存在");
        }
        return ApiResponse.success(convertToReportDTO(report));
    }

    @Override
    public ApiResponse<List<ReportDTO>> getReportsByStudentId(Long userId) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null) {
            throw new BusinessException("学生档案不存在");
        }
        List<AssessmentReport> reports = reportMapper.findByStudentId(studentProfileId);
        List<ReportDTO> dtoList = reports.stream()
                .map(this::convertToReportDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    private Long generateReport(Long recordId, Long studentId, double score, String dimension) {
        String content = String.format("{\"dimension\":\"%s\",\"score\":%.2f,\"level\":\"%s\"}",
                dimension, score, getLevel(score));
        String recommendations = generateRecommendations(dimension, score);

        AssessmentReport report = AssessmentReport.builder()
                .recordId(recordId)
                .studentId(studentId)
                .content(content)
                .recommendations(recommendations)
                .generatedAt(LocalDateTime.now())
                .build();
        reportMapper.insert(report);
        return report.getId();
    }

    private String getLevel(double score) {
        if (score >= 80) return "优秀";
        if (score >= 60) return "良好";
        if (score >= 40) return "中等";
        return "需提升";
    }

    private String generateRecommendations(String dimension, double score) {
        String baseRecommendation;
        if (score >= 80) {
            baseRecommendation = "您在" + dimension + "维度表现优秀，建议继续保持并深入发展相关能力。";
        } else if (score >= 60) {
            baseRecommendation = "您在" + dimension + "维度表现良好，有一定的发展潜力。";
        } else if (score >= 40) {
            baseRecommendation = "您在" + dimension + "维度需要加强，可以通过学习和实践提升。";
        } else {
            baseRecommendation = "建议您重点关注" + dimension + "维度的能力提升，制定学习计划。";
        }
        return baseRecommendation;
    }

    private int parseAnswerValue(String answer) {
        try {
            return Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private AssessmentDTO convertToDTO(Assessment entity) {
        return AssessmentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .dimension(entity.getDimension())
                .duration(entity.getDuration())
                .status(entity.getStatus())
                .build();
    }

    private QuestionDTO convertToQuestionDTO(AssessmentQuestion entity) {
        return QuestionDTO.builder()
                .id(entity.getId())
                .assessmentId(entity.getAssessmentId())
                .questionType(entity.getQuestionType())
                .content(entity.getContent())
                .options(entity.getOptions())
                .weight(entity.getWeight())
                .sortOrder(entity.getSortOrder())
                .build();
    }

    private ReportDTO convertToReportDTO(AssessmentReport entity) {
        return ReportDTO.builder()
                .id(entity.getId())
                .recordId(entity.getRecordId())
                .studentId(entity.getStudentId())
                .content(entity.getContent())
                .recommendations(entity.getRecommendations())
                .generatedAt(entity.getGeneratedAt())
                .build();
    }
}