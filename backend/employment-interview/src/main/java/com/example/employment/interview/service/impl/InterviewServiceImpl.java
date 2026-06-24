package com.example.employment.interview.service.impl;

import com.example.employment.common.exception.BusinessException;
import com.example.employment.interview.entity.Interview;
import com.example.employment.interview.entity.InterviewEvaluation;
import com.example.employment.interview.mapper.InterviewEvaluationMapper;
import com.example.employment.interview.mapper.InterviewMapper;
import com.example.employment.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewMapper interviewMapper;
    private final InterviewEvaluationMapper evaluationMapper;

    @Override
    public List<Interview> listAll(Long applicationId) {
        if (applicationId != null) {
            return interviewMapper.findByApplicationId(applicationId);
        }
        return interviewMapper.selectList(null);
    }

    @Override
    public Interview getById(Long id) {
        Interview interview = interviewMapper.selectById(id);
        if (interview == null) {
            throw new BusinessException("面试记录不存在");
        }
        return interview;
    }

    @Override
    @Transactional
    public Interview createInterview(Interview interview) {
        interview.setCreatedAt(LocalDateTime.now());
        interview.setUpdatedAt(LocalDateTime.now());
        interviewMapper.insert(interview);
        return interview;
    }

    @Override
    @Transactional
    public Interview updateInterview(Long id, Interview interview) {
        Interview existing = interviewMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("面试记录不存在");
        }
        // 只更新非空字段
        if (interview.getApplicationId() != null) existing.setApplicationId(interview.getApplicationId());
        if (interview.getType() != null) existing.setType(interview.getType());
        if (interview.getScheduledTime() != null) existing.setScheduledTime(interview.getScheduledTime());
        if (interview.getStatus() != null) existing.setStatus(interview.getStatus());
        if (interview.getMeetingUrl() != null) existing.setMeetingUrl(interview.getMeetingUrl());
        if (interview.getRecordingUrl() != null) existing.setRecordingUrl(interview.getRecordingUrl());
        existing.setUpdatedAt(LocalDateTime.now());
        interviewMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void deleteInterview(Long id) {
        Interview existing = interviewMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("面试记录不存在");
        }
        interviewMapper.deleteById(id);
    }

    @Override
    public List<Interview> getByStudentId(Long studentProfileId) {
        return interviewMapper.findByStudentId(studentProfileId);
    }

    @Override
    @Transactional
    public InterviewEvaluation addEvaluation(Long interviewId, Long evaluatorId, InterviewEvaluation evaluation) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException("面试记录不存在");
        }
        evaluation.setInterviewId(interviewId);
        evaluation.setEvaluatorId(evaluatorId);
        evaluation.setCreatedAt(LocalDateTime.now());
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    @Override
    public List<InterviewEvaluation> getEvaluationsByInterviewId(Long interviewId) {
        return evaluationMapper.findByInterviewId(interviewId);
    }

    @Override
    @Transactional
    public void deleteEvaluation(Long evaluationId) {
        InterviewEvaluation evaluation = evaluationMapper.selectById(evaluationId);
        if (evaluation == null) {
            throw new BusinessException("面试评估不存在");
        }
        evaluationMapper.deleteById(evaluationId);
    }
}