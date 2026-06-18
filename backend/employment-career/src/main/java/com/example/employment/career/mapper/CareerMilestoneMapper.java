package com.example.employment.career.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.career.entity.CareerMilestone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CareerMilestoneMapper extends BaseMapper<CareerMilestone> {
    List<CareerMilestone> findByPlanId(Long planId);
}