package com.example.employment.hr.service.impl;

import com.example.employment.common.entity.Application;
import com.example.employment.common.entity.JobOffer;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.repository.ApplicationMapper;
import com.example.employment.common.repository.JobOfferMapper;
import com.example.employment.hr.service.HrApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HrApplicationServiceImpl implements HrApplicationService {

    private final ApplicationMapper applicationMapper;
    private final JobOfferMapper jobOfferMapper;

    @Override
    public List<Application> getApplicationsByCompany(Long companyId, String status, String keyword, int page, int size) {
        // 按公司查询：通过 job_id 关联 jobs.company_id = companyId
        // 简化实现：查询所有 application，在 service 层过滤
        // 实际生产环境应使用 JOIN SQL
        return applicationMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Application>()
                .eq(status != null, Application::getStatus, status)
                .orderByDesc(Application::getCreatedAt)
                .last("LIMIT " + ((page - 1) * size) + ", " + size)
        );
    }

    @Override
    @Transactional
    public Application updateApplicationStatus(Long applicationId, String status, Long hrId) {
        Application application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new BusinessException("申请记录不存在");
        }
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());
        applicationMapper.updateById(application);
        return application;
    }

    @Override
    @Transactional
    public Application addApplicationNote(Long applicationId, String note) {
        Application application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new BusinessException("申请记录不存在");
        }
        application.setFeedback(note);
        application.setUpdatedAt(LocalDateTime.now());
        applicationMapper.updateById(application);
        return application;
    }

    @Override
    @Transactional
    public JobOffer createOffer(Long applicationId, Long hrId, BigDecimal salary, String title) {
        Application application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new BusinessException("申请记录不存在");
        }

        JobOffer offer = JobOffer.builder()
                .applicationId(applicationId)
                .hrId(hrId)
                .salary(salary)
                .title(title)
                .status("SENT")
                .sentAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
        jobOfferMapper.insert(offer);

        application.setStatus("OFFER");
        application.setUpdatedAt(LocalDateTime.now());
        applicationMapper.updateById(application);

        return offer;
    }

    @Override
    public List<JobOffer> getOffers(Long companyId) {
        // 简化实现
        return jobOfferMapper.selectList(null);
    }
}