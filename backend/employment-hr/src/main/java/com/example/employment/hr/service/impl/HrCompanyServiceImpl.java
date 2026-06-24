package com.example.employment.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.employment.common.entity.CompanyHrMember;
import com.example.employment.common.entity.CompanyProfile;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.hr.mapper.CompanyHrMemberMapper;
import com.example.employment.hr.service.HrCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HrCompanyServiceImpl implements HrCompanyService {

    private final CompanyHrMemberMapper companyHrMemberMapper;
    private final com.example.employment.common.repository.CompanyProfileMapper companyProfileMapper;

    @Override
    public CompanyProfile getCompanyByUserId(Long userId) {
        List<CompanyHrMember> memberships = companyHrMemberMapper.findByUserId(userId);
        if (memberships.isEmpty()) {
            throw new BusinessException("您尚未关联任何公司");
        }
        CompanyHrMember membership = memberships.get(0);
        return companyProfileMapper.selectById(membership.getCompanyId());
    }

    @Override
    public List<CompanyHrMember> getTeamMembers(Long companyId) {
        return companyHrMemberMapper.findByCompanyId(companyId);
    }

    @Override
    @Transactional
    public CompanyHrMember inviteMember(Long companyId, Long userId, String role) {
        CompanyHrMember existing = companyHrMemberMapper.findByCompanyAndUser(companyId, userId);
        if (existing != null) {
            throw new BusinessException("该用户已是公司成员");
        }
        CompanyHrMember member = CompanyHrMember.builder()
                .companyId(companyId)
                .userId(userId)
                .role(role != null ? role : "MEMBER")
                .accepted(false)
                .invitedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        companyHrMemberMapper.insert(member);
        return member;
    }

    @Override
    @Transactional
    public void removeMember(Long memberId, Long operatorUserId) {
        CompanyHrMember member = companyHrMemberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException("成员不存在");
        }
        if ("OWNER".equals(member.getRole())) {
            throw new BusinessException("不能移除公司所有者");
        }
        companyHrMemberMapper.deleteById(memberId);
    }

    @Override
    @Transactional
    public void updateMemberRole(Long memberId, String role) {
        CompanyHrMember member = companyHrMemberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException("成员不存在");
        }
        member.setRole(role);
        member.setUpdatedAt(LocalDateTime.now());
        companyHrMemberMapper.updateById(member);
    }

    @Override
    public boolean isHrOfCompany(Long userId, Long companyId) {
        CompanyHrMember member = companyHrMemberMapper.findByCompanyAndUser(companyId, userId);
        return member != null && Boolean.TRUE.equals(member.getAccepted());
    }
}