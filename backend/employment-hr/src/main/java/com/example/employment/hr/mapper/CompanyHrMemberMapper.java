package com.example.employment.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.common.entity.CompanyHrMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyHrMemberMapper extends BaseMapper<CompanyHrMember> {

    List<CompanyHrMember> findByCompanyId(@Param("companyId") Long companyId);

    List<CompanyHrMember> findByUserId(@Param("userId") Long userId);

    CompanyHrMember findByCompanyAndUser(@Param("companyId") Long companyId, @Param("userId") Long userId);
}