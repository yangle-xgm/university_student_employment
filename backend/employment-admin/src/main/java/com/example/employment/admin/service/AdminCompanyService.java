package com.example.employment.admin.service;

import com.example.employment.common.entity.CompanyProfile;

import java.util.List;

public interface AdminCompanyService {

    List<CompanyProfile> getUnverifiedCompanies();

    void verifyCompany(Long companyId, Long adminUserId);

    void rejectCompany(Long companyId);
}