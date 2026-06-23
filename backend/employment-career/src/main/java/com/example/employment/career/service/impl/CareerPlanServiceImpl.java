package com.example.employment.career.service.impl;

import com.example.employment.career.entity.CareerPlan;
import com.example.employment.career.mapper.CareerPlanMapper;
import com.example.employment.career.service.CareerPlanService;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareerPlanServiceImpl implements CareerPlanService {

    private final CareerPlanMapper careerPlanMapper;
    private final StudentProfileService studentProfileService;

    @Override
    public List<CareerPlan> getPlansByUserId(Long userId) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null) {
            return List.of();
        }
        return careerPlanMapper.findByStudentId(studentProfileId);
    }

    @Override
    @Transactional
    public CareerPlan createPlan(Long userId, CareerPlan plan) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, true);
        if (studentProfileId == null) {
            throw new BusinessException("无法获取学生档案ID");
        }
        plan.setStudentId(studentProfileId);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        careerPlanMapper.insert(plan);
        return plan;
    }

    @Override
    @Transactional
    public void deletePlan(Long userId, Long planId) {
        CareerPlan plan = careerPlanMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null || !studentProfileId.equals(plan.getStudentId())) {
            throw new BusinessException("无权限删除该职业规划");
        }
        careerPlanMapper.deleteById(planId);
    }
}
