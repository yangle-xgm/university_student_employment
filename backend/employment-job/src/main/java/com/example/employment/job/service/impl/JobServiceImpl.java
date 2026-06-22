package com.example.employment.job.service.impl;

import com.example.employment.common.exception.BusinessException;
import com.example.employment.job.entity.Job;
import com.example.employment.job.repository.JobMapper;
import com.example.employment.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    @Override
    public Job getJobById(Long jobId) {
        Job job = jobMapper.selectJobWithCompany(jobId);
        if (job == null) {
            throw new BusinessException("职位不存在");
        }
        return job;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobMapper.selectAllJobsWithCompany();
    }

    @Override
    public List<Job> searchJobs(String keyword, String location, String industry) {
        return jobMapper.searchJobs(keyword, location, industry);
    }

    @Override
    @Transactional
    public Job createJob(Job job) {
        job.setStatus("PUBLISHED");
        jobMapper.insert(job);
        return jobMapper.selectJobWithCompany(job.getId());
    }

    @Override
    @Transactional
    public Job updateJob(Long jobId, Job job) {
        Job existing = jobMapper.selectById(jobId);
        if (existing == null) {
            throw new BusinessException("职位不存在");
        }
        job.setId(jobId);
        job.setCompanyName(null);
        jobMapper.updateById(job);
        return jobMapper.selectJobWithCompany(jobId);
    }

    @Override
    @Transactional
    public void deleteJob(Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException("职位不存在");
        }
        jobMapper.deleteById(jobId);
    }
}