package com.example.employment.career.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.career.entity.CareerPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CareerPlanMapper extends BaseMapper<CareerPlan> {
    List<CareerPlan> findByStudentId(Long studentId);
}