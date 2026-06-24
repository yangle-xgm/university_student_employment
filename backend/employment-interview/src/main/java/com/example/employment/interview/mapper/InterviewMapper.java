package com.example.employment.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.interview.entity.Interview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InterviewMapper extends BaseMapper<Interview> {

    List<Interview> findByApplicationId(@Param("applicationId") Long applicationId);

    List<Interview> findByStudentId(@Param("studentProfileId") Long studentProfileId);
}