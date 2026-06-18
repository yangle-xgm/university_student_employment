package com.example.employment.interview.service.impl;

import com.example.employment.interview.dto.request.CreateInterviewRequest;
import com.example.employment.interview.dto.request.UpdateInterviewRequest;
import com.example.employment.interview.dto.response.InterviewDTO;
import com.example.employment.interview.entity.Interview;
import com.example.employment.interview.mapper.InterviewMapper;
import com.example.employment.interview.service.InterviewService;
import com.example.employment.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewMapper interviewMapper;

    @Override
    public InterviewDTO getInterviewById(Long interviewId) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException("面试不存在");
        }
        return convertToDTO(interview);
    }

    @Override
    public List<InterviewDTO> getAllInterviews() {
        List<Interview> interviews = interviewMapper.selectList(null);
        return interviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDTO> getInterviewsByUserId(Long userId) {
        List<Interview> interviews = interviewMapper.findByUserId(userId);
        return interviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDTO> getInterviewsByJobId(Long jobId) {
        List<Interview> interviews = interviewMapper.findByJobId(jobId);
        return interviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InterviewDTO createInterview(CreateInterviewRequest request) {
        Interview interview = Interview.builder()
                .userId(request.getUserId())
                .jobId(request.getJobId())
                .interviewTime(request.getInterviewTime())
                .location(request.getLocation())
                .status("SCHEDULED")
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        interviewMapper.insert(interview);
        return convertToDTO(interview);
    }

    @Override
    @Transactional
    public InterviewDTO updateInterview(Long interviewId, UpdateInterviewRequest request) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException("面试不存在");
        }

        if (request.getInterviewTime() != null) {
            interview.setInterviewTime(request.getInterviewTime());
        }
        if (request.getLocation() != null) {
            interview.setLocation(request.getLocation());
        }
        if (request.getStatus() != null) {
            interview.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            interview.setNotes(request.getNotes());
        }
        interview.setUpdatedAt(LocalDateTime.now());

        interviewMapper.updateById(interview);
        return convertToDTO(interview);
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

    private InterviewDTO convertToDTO(Interview entity) {
        return InterviewDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .jobId(entity.getJobId())
                .interviewTime(entity.getInterviewTime())
                .location(entity.getLocation())
                .notes(entity.getNotes())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
