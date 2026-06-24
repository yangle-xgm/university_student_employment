package com.example.employment.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.employment.admin.service.AdminCompanyService;
import com.example.employment.common.entity.CompanyProfile;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.repository.CompanyProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompanyServiceImpl implements AdminCompanyService {

    private final CompanyProfileMapper companyProfileMapper;

    @Override
    public List<CompanyProfile> getUnverifiedCompanies() {
        return companyProfileMapper.selectList(
            new LambdaQueryWrapper<CompanyProfile>()
                .eq(CompanyProfile::getVerified, false)
                .or()
                .isNull(CompanyProfile::getVerified)
        );
    }

    @Override
    @Transactional
    public void verifyCompany(Long companyId, Long adminUserId) {
        CompanyProfile company = companyProfileMapper.selectById(companyId);
        if (company == null) {
            throw new BusinessException("公司不存在");
        }
        company.setVerified(true);
        company.setVerifiedAt(LocalDateTime.now());
        company.setVerifiedBy(adminUserId);
        companyProfileMapper.updateById(company);
    }

    @Override
    @Transactional
    public void rejectCompany(Long companyId) {
        CompanyProfile company = companyProfileMapper.selectById(companyId);
        if (company == null) {
            throw new BusinessException("公司不存在");
        }
        company.setVerified(false);
        company.setVerifiedAt(LocalDateTime.now());
        companyProfileMapper.updateById(company);
    }
}