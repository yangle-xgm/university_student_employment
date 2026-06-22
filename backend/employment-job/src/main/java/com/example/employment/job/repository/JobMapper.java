package com.example.employment.job.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.job.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobMapper extends BaseMapper<Job> {

    List<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("industry") String industry);

    Job selectJobWithCompany(@Param("id") Long id);

    List<Job> selectAllJobsWithCompany();
}