package com.example.employment.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.assessment.entity.Assessment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssessmentMapper extends BaseMapper<Assessment> {
    List<Assessment> findByStatus(String status);
}