package com.example.employment.job.service.impl;

import com.example.employment.common.exception.BusinessException;
import com.example.employment.job.dto.request.CreateJobRequest;
import com.example.employment.job.dto.request.UpdateJobRequest;
import com.example.employment.job.dto.response.JobDTO;
import com.example.employment.job.entity.Job;
import com.example.employment.job.repository.JobMapper;
import com.example.employment.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    @Override
    public JobDTO getJobById(Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException("职位不存在");
        }
        return convertToDTO(job);
    }

    @Override
    public List<JobDTO> getAllJobs() {
        List<Job> jobs = jobMapper.selectList(null);
        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobDTO> searchJobs(String keyword, String location, String industry) {
        List<Job> jobs = jobMapper.searchJobs(keyword, location, industry);
        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobDTO createJob(CreateJobRequest request) {
        Job job = Job.builder()
                .title(request.getTitle())
                .companyName(request.getCompanyName())
                .location(request.getLocation())
                .industry(request.getIndustry())
                .description(request.getDescription())
                .responsibilities(request.getResponsibilities())
                .requirements(request.getRequirements())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .experience(request.getExperience())
                .education(request.getEducation())
                .benefits(request.getBenefits())
                .recruitmentCount(request.getRecruitmentCount())
                .status("ACTIVE")
                .build();
        
        jobMapper.insert(job);
        return convertToDTO(job);
    }

    @Override
    @Transactional
    public JobDTO updateJob(Long jobId, UpdateJobRequest request) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException("职位不存在");
        }

        if (StringUtils.hasText(request.getTitle())) {
            job.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(request.getCompanyName())) {
            job.setCompanyName(request.getCompanyName());
        }
        if (StringUtils.hasText(request.getLocation())) {
            job.setLocation(request.getLocation());
        }
        if (StringUtils.hasText(request.getIndustry())) {
            job.setIndustry(request.getIndustry());
        }
        if (StringUtils.hasText(request.getDescription())) {
            job.setDescription(request.getDescription());
        }
        if (StringUtils.hasText(request.getResponsibilities())) {
            job.setResponsibilities(request.getResponsibilities());
        }
        if (StringUtils.hasText(request.getRequirements())) {
            job.setRequirements(request.getRequirements());
        }
        if (request.getMinSalary() != null) {
            job.setMinSalary(request.getMinSalary());
        }
        if (request.getMaxSalary() != null) {
            job.setMaxSalary(request.getMaxSalary());
        }
        if (StringUtils.hasText(request.getExperience())) {
            job.setExperience(request.getExperience());
        }
        if (StringUtils.hasText(request.getEducation())) {
            job.setEducation(request.getEducation());
        }
        if (StringUtils.hasText(request.getBenefits())) {
            job.setBenefits(request.getBenefits());
        }
        if (request.getRecruitmentCount() != null) {
            job.setRecruitmentCount(request.getRecruitmentCount());
        }
        if (StringUtils.hasText(request.getStatus())) {
            job.setStatus(request.getStatus());
        }

        jobMapper.updateById(job);
        return convertToDTO(job);
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

    private JobDTO convertToDTO(Job job) {
        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .companyName(job.getCompanyName())
                .location(job.getLocation())
                .industry(job.getIndustry())
                .description(job.getDescription())
                .responsibilities(job.getResponsibilities())
                .requirements(job.getRequirements())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .experience(job.getExperience())
                .education(job.getEducation())
                .benefits(job.getBenefits())
                .recruitmentCount(job.getRecruitmentCount())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}