package com.example.employment.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.common.entity.JobOffer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobOfferMapper extends BaseMapper<JobOffer> {

    List<JobOffer> findByApplicationId(@Param("applicationId") Long applicationId);

    List<JobOffer> findByHrId(@Param("hrId") Long hrId);

    List<JobOffer> findByCompanyId(@Param("companyId") Long companyId);
}