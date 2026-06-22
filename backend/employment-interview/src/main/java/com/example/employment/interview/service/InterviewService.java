package com.example.employment.interview.service;

import com.example.employment.interview.entity.Interview;

import java.util.List;

public interface InterviewService {
    Interview getInterviewById(Long interviewId);
    List<Interview> getAllInterviews();
    List<Interview> getInterviewsByApplicationId(Long applicationId);
    Interview createInterview(Interview interview);
    Interview updateInterview(Long interviewId, Interview interview);
    void deleteInterview(Long interviewId);
}