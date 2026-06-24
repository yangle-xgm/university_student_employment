package com.example.employment.job.service;

import com.example.employment.common.entity.Application;

import java.util.List;

public interface ApplicationService {

    List<Application> getApplicationsByStudentId(Long userId);

    List<Application> getApplicationsByJobId(Long jobId);

    Application apply(Long userId, Long jobId, Long resumeId);

    Application updateApplicationStatus(Long applicationId, String status);
}