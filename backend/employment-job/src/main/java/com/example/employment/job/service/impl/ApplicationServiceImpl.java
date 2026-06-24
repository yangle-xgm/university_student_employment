package com.example.employment.job.service.impl;

import com.example.employment.common.entity.Application;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.service.StudentProfileService;
import com.example.employment.job.repository.ApplicationMapper;
import com.example.employment.job.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final StudentProfileService studentProfileService;

    @Override
    public List<Application> getApplicationsByStudentId(Long userId) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null) {
            return Collections.emptyList();
        }
        return applicationMapper.findByStudentId(studentProfileId);
    }

    @Override
    public List<Application> getApplicationsByJobId(Long jobId) {
        return applicationMapper.findByJobId(jobId);
    }

    @Override
    @Transactional
    public Application apply(Long userId, Long jobId, Long resumeId) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, true);
        if (studentProfileId == null) {
            throw new BusinessException("无法获取学生档案ID");
        }

        Application application = Application.builder()
                .jobId(jobId)
                .studentId(studentProfileId)
                .resumeId(resumeId)
                .status("PENDING")
                .applyTime(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        applicationMapper.insert(application);
        return application;
    }

    @Override
    @Transactional
    public Application updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new BusinessException("申请记录不存在");
        }
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        applicationMapper.updateById(application);
        return application;
    }
}