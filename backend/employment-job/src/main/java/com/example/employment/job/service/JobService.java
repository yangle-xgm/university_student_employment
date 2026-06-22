package com.example.employment.job.service;

import com.example.employment.job.entity.Job;

import java.util.List;

public interface JobService {
    Job getJobById(Long jobId);
    List<Job> getAllJobs();
    List<Job> searchJobs(String keyword, String location, String industry);
    Job createJob(Job job);
    Job updateJob(Long jobId, Job job);
    void deleteJob(Long jobId);
}