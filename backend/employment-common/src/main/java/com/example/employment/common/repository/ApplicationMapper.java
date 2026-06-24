package com.example.employment.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.common.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {

    List<Application> findByStudentId(@Param("studentId") Long studentId);

    List<Application> findByJobId(@Param("jobId") Long jobId);

    List<Application> findByCompanyId(@Param("companyId") Long companyId,
                                       @Param("status") String status,
                                       @Param("offset") int offset,
                                       @Param("size") int size);
}