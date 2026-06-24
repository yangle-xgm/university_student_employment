package com.example.employment.hr.service;

import com.example.employment.common.entity.CompanyHrMember;
import com.example.employment.common.entity.CompanyProfile;

import java.util.List;

public interface HrCompanyService {

    CompanyProfile getCompanyByUserId(Long userId);

    List<CompanyHrMember> getTeamMembers(Long companyId);

    CompanyHrMember inviteMember(Long companyId, Long userId, String role);

    void removeMember(Long memberId, Long operatorUserId);

    void updateMemberRole(Long memberId, String role, Long operatorUserId);

    boolean isHrOfCompany(Long userId, Long companyId);
}