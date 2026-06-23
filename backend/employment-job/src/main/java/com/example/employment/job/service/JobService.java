package com.example.employment.job.service;

import com.example.employment.job.dto.response.JobDTO;
import com.example.employment.job.entity.Job;

import java.util.List;

public interface JobService {
    JobDTO getJobById(Long jobId);
    List<JobDTO> getAllJobs();
    List<JobDTO> searchJobs(String keyword, String location, String industry);
    JobDTO createJob(Job job);
    JobDTO updateJob(Long jobId, Job job);
    void deleteJob(Long jobId);
}