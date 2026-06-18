package com.example.employment.interview.service;

import com.example.employment.interview.dto.request.CreateInterviewRequest;
import com.example.employment.interview.dto.request.UpdateInterviewRequest;
import com.example.employment.interview.dto.response.InterviewDTO;

import java.util.List;

public interface InterviewService {
    InterviewDTO getInterviewById(Long interviewId);
    List<InterviewDTO> getAllInterviews();
    List<InterviewDTO> getInterviewsByUserId(Long userId);
    List<InterviewDTO> getInterviewsByJobId(Long jobId);
    InterviewDTO createInterview(CreateInterviewRequest request);
    InterviewDTO updateInterview(Long interviewId, UpdateInterviewRequest request);
    void deleteInterview(Long interviewId);
}