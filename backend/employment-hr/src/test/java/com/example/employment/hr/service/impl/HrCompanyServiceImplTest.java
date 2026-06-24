package com.example.employment.hr.service.impl;

import com.example.employment.common.entity.CompanyHrMember;
import com.example.employment.common.entity.CompanyProfile;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.repository.CompanyProfileMapper;
import com.example.employment.hr.mapper.CompanyHrMemberMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HrCompanyServiceImplTest {

    @Mock
    private CompanyHrMemberMapper companyHrMemberMapper;

    @Mock
    private CompanyProfileMapper companyProfileMapper;

    @InjectMocks
    private HrCompanyServiceImpl hrCompanyService;

    @Test
    void removeMember_ThrowsWhenOperatorNotInTargetCompany() {
        Long memberId = 1L;
        Long operatorUserId = 100L;
        CompanyHrMember member = CompanyHrMember.builder()
                .id(memberId)
                .companyId(1L)
                .userId(200L)
                .role("MEMBER")
                .accepted(true)
                .build();

        when(companyHrMemberMapper.selectById(memberId)).thenReturn(member);
        when(companyHrMemberMapper.findByCompanyAndUser(member.getCompanyId(), operatorUserId)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> hrCompanyService.removeMember(memberId, operatorUserId));

        assertEquals("无权操作该公司成员", exception.getMessage());
        verify(companyHrMemberMapper, never()).deleteById(anyLong());
    }

    @Test
    void updateMemberRole_ThrowsWhenOperatorNotInTargetCompany() {
        Long memberId = 2L;
        Long operatorUserId = 100L;
        CompanyHrMember member = CompanyHrMember.builder()
                .id(memberId)
                .companyId(1L)
                .userId(200L)
                .role("MEMBER")
                .accepted(true)
                .build();

        when(companyHrMemberMapper.selectById(memberId)).thenReturn(member);
        when(companyHrMemberMapper.findByCompanyAndUser(member.getCompanyId(), operatorUserId)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> hrCompanyService.updateMemberRole(memberId, "ADMIN", operatorUserId));

        assertEquals("无权操作该公司成员", exception.getMessage());
        verify(companyHrMemberMapper, never()).updateById(any(CompanyHrMember.class));
    }
}
