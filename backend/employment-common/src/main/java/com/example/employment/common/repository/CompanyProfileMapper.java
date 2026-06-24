package com.example.employment.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.common.entity.CompanyProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyProfileMapper extends BaseMapper<CompanyProfile> {
}