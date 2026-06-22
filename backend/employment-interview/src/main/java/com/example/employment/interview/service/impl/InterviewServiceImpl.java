package com.example.employment.interview.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.interview.entity.Interview;
import com.example.employment.interview.mapper.InterviewMapper;
import com.example.employment.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewMapper interviewMapper;

    @Override
    public Interview getInterviewById(Long interviewId) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException("面试不存在");
        }
        return interview;
    }

    @Override
    public List<Interview> getAllInterviews() {
        return interviewMapper.selectList(null);
    }

    @Override
    public List<Interview> getInterviewsByApplicationId(Long applicationId) {
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Interview::getApplicationId, applicationId)
               .orderByDesc(Interview::getCreatedAt);
        return interviewMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public Interview createInterview(Interview interview) {
        interview.setStatus("SCHEDULED");
        interviewMapper.insert(interview);
        return interview;
    }

    @Override
    @Transactional
    public Interview updateInterview(Long interviewId, Interview interview) {
        Interview existing = interviewMapper.selectById(interviewId);
        if (existing == null) {
            throw new BusinessException("面试不存在");
        }
        interview.setId(interviewId);
        interviewMapper.updateById(interview);
        return interviewMapper.selectById(interviewId);
    }

    @Override
    @Transactional
    public void deleteInterview(Long interviewId) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException("面试不存在");
        }
        interviewMapper.deleteById(interviewId);
    }
}