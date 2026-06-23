package com.example.employment.job.service.impl;

import com.example.employment.common.exception.BusinessException;
import com.example.employment.job.dto.response.JobDTO;
import com.example.employment.job.entity.Job;
import com.example.employment.job.repository.JobMapper;
import com.example.employment.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;

    @Override
    public JobDTO getJobById(Long jobId) {
        Job job = jobMapper.selectJobWithCompany(jobId);
        if (job == null) {
            throw new BusinessException("职位不存在");
        }
        return convertToDTO(job);
    }

    @Override
    public List<JobDTO> getAllJobs() {
        return jobMapper.selectAllJobsWithCompany().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobDTO> searchJobs(String keyword, String location, String industry) {
        return jobMapper.searchJobs(keyword, location, industry).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobDTO createJob(Job job) {
        job.setStatus("PUBLISHED");
        jobMapper.insert(job);
        Job created = jobMapper.selectJobWithCompany(job.getId());
        return convertToDTO(created);
    }

    @Override
    @Transactional
    public JobDTO updateJob(Long jobId, Job job) {
        Job existing = jobMapper.selectById(jobId);
        if (existing == null) {
            throw new BusinessException("职位不存在");
        }
        job.setId(jobId);
        job.setCompanyName(null);
        jobMapper.updateById(job);
        Job updated = jobMapper.selectJobWithCompany(jobId);
        return convertToDTO(updated);
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
                .companyName(job.getCompanyName() != null ? job.getCompanyName() : "")
                .location(job.getLocation() != null ? job.getLocation() : "")
                .industry(job.getIndustry() != null ? job.getIndustry() : "")
                .description(job.getDescription() != null ? job.getDescription() : "")
                .responsibilities(job.getResponsibilities() != null ? job.getResponsibilities() : "")
                .requirements(job.getRequirements() != null ? job.getRequirements() : "")
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .experience(job.getExperience() != null ? job.getExperience() : "")
                .education(job.getEducation() != null ? job.getEducation() : "")
                .benefits(job.getBenefits() != null ? job.getBenefits() : "")
                .recruitmentCount(job.getRecruitmentCount())
                .status(job.getStatus() != null ? job.getStatus() : "")
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}