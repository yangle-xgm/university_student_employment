package com.example.employment.interview.service;

import com.example.employment.interview.entity.Interview;
import com.example.employment.interview.entity.InterviewEvaluation;

import java.util.List;

public interface InterviewService {

    List<Interview> listAll(Long applicationId);

    Interview getById(Long id);

    Interview createInterview(Interview interview);

    Interview updateInterview(Long id, Interview interview);

    void deleteInterview(Long id);

    List<Interview> getByStudentId(Long studentProfileId);

    // 面试评估
    InterviewEvaluation addEvaluation(Long interviewId, Long evaluatorId, InterviewEvaluation evaluation);

    List<InterviewEvaluation> getEvaluationsByInterviewId(Long interviewId);

    void deleteEvaluation(Long evaluationId);
}