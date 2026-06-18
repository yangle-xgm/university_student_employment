package com.example.employment.job.service;

import com.example.employment.job.dto.request.CreateJobRequest;
import com.example.employment.job.dto.request.UpdateJobRequest;
import com.example.employment.job.dto.response.JobDTO;

import java.util.List;

public interface JobService {
    JobDTO getJobById(Long jobId);
    List<JobDTO> getAllJobs();
    List<JobDTO> searchJobs(String keyword, String location, String industry);
    JobDTO createJob(CreateJobRequest request);
    JobDTO updateJob(Long jobId, UpdateJobRequest request);
    void deleteJob(Long jobId);
}