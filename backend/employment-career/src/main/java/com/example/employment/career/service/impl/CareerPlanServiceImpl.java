package com.example.employment.career.service.impl;

import com.example.employment.career.dto.CareerMilestoneDTO;
import com.example.employment.career.dto.CareerPlanDTO;
import com.example.employment.career.dto.CreateCareerPlanRequest;
import com.example.employment.career.dto.CreateMilestoneRequest;
import com.example.employment.career.entity.CareerMilestone;
import com.example.employment.career.entity.CareerPlan;
import com.example.employment.career.mapper.CareerMilestoneMapper;
import com.example.employment.career.mapper.CareerPlanMapper;
import com.example.employment.career.service.CareerPlanService;
import com.example.employment.common.dto.response.ApiResponse;
import com.example.employment.common.exception.BusinessException;
import com.example.employment.common.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareerPlanServiceImpl implements CareerPlanService {

    private final CareerPlanMapper careerPlanMapper;
    private final CareerMilestoneMapper careerMilestoneMapper;
    private final StudentProfileService studentProfileService;

    @Override
    public ApiResponse<List<CareerPlanDTO>> getPlansByStudentId(Long userId) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null) {
            return ApiResponse.success(List.of());
        }
        List<CareerPlan> plans = careerPlanMapper.findByStudentId(studentProfileId);
        List<CareerPlanDTO> dtos = plans.stream().map(this::toPlanDTO).collect(Collectors.toList());
        return ApiResponse.success(dtos);
    }

    @Override
    public ApiResponse<CareerPlanDTO> getPlanById(Long planId) {
        CareerPlan plan = careerPlanMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        return ApiResponse.success(toPlanDTO(plan));
    }

    @Override
    @Transactional
    public ApiResponse<CareerPlanDTO> createPlan(Long userId, CreateCareerPlanRequest request) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, true);
        if (studentProfileId == null) {
            throw new BusinessException("无法获取学生档案ID");
        }
        CareerPlan plan = CareerPlan.builder()
                .studentId(studentProfileId)
                .title(request.getTitle())
                .goalShort(request.getGoalShort())
                .goalMedium(request.getGoalMedium())
                .goalLong(request.getGoalLong())
                .currentStage(request.getCurrentStage())
                .progress(0.0)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        careerPlanMapper.insert(plan);
        return ApiResponse.success(toPlanDTO(plan));
    }

    @Override
    @Transactional
    public ApiResponse<CareerPlanDTO> updatePlan(Long planId, CreateCareerPlanRequest request) {
        CareerPlan plan = careerPlanMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        if (request.getTitle() != null) plan.setTitle(request.getTitle());
        if (request.getGoalShort() != null) plan.setGoalShort(request.getGoalShort());
        if (request.getGoalMedium() != null) plan.setGoalMedium(request.getGoalMedium());
        if (request.getGoalLong() != null) plan.setGoalLong(request.getGoalLong());
        if (request.getCurrentStage() != null) plan.setCurrentStage(request.getCurrentStage());
        plan.setUpdatedAt(LocalDateTime.now());
        careerPlanMapper.updateById(plan);
        return ApiResponse.success(toPlanDTO(plan));
    }

    @Override
    @Transactional
    public ApiResponse<String> deletePlan(Long planId) {
        CareerPlan plan = careerPlanMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        careerPlanMapper.deleteById(planId);
        return ApiResponse.success("删除成功");
    }

    @Override
    public ApiResponse<List<CareerMilestoneDTO>> getMilestonesByPlanId(Long planId) {
        List<CareerMilestone> milestones = careerMilestoneMapper.findByPlanId(planId);
        List<CareerMilestoneDTO> dtos = milestones.stream().map(this::toMilestoneDTO).collect(Collectors.toList());
        return ApiResponse.success(dtos);
    }

    @Override
    @Transactional
    public ApiResponse<CareerMilestoneDTO> addMilestone(Long planId, CreateMilestoneRequest request) {
        CareerMilestone milestone = CareerMilestone.builder()
                .planId(planId)
                .title(request.getTitle())
                .description(request.getDescription())
                .targetDate(request.getTargetDate())
                .progress(0.0)
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        careerMilestoneMapper.insert(milestone);
        return ApiResponse.success(toMilestoneDTO(milestone));
    }

    @Override
    @Transactional
    public ApiResponse<CareerMilestoneDTO> updateMilestone(Long milestoneId, CreateMilestoneRequest request) {
        CareerMilestone milestone = careerMilestoneMapper.selectById(milestoneId);
        if (milestone == null) {
            throw new BusinessException("里程碑不存在");
        }
        if (request.getTitle() != null) milestone.setTitle(request.getTitle());
        if (request.getDescription() != null) milestone.setDescription(request.getDescription());
        if (request.getTargetDate() != null) milestone.setTargetDate(request.getTargetDate());
        milestone.setUpdatedAt(LocalDateTime.now());
        careerMilestoneMapper.updateById(milestone);
        return ApiResponse.success(toMilestoneDTO(milestone));
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteMilestone(Long milestoneId) {
        CareerMilestone milestone = careerMilestoneMapper.selectById(milestoneId);
        if (milestone == null) {
            throw new BusinessException("里程碑不存在");
        }
        careerMilestoneMapper.deleteById(milestoneId);
        return ApiResponse.success("删除成功");
    }

    @Override
    @Transactional
    public ApiResponse<CareerMilestoneDTO> updateMilestoneProgress(Long milestoneId, Double progress) {
        CareerMilestone milestone = careerMilestoneMapper.selectById(milestoneId);
        if (milestone == null) {
            throw new BusinessException("里程碑不存在");
        }
        milestone.setProgress(progress);
        milestone.setCompleted(progress >= 100.0);
        milestone.setUpdatedAt(LocalDateTime.now());
        careerMilestoneMapper.updateById(milestone);
        return ApiResponse.success(toMilestoneDTO(milestone));
    }

    private CareerPlanDTO toPlanDTO(CareerPlan plan) {
        return CareerPlanDTO.builder()
                .id(plan.getId())
                .studentId(plan.getStudentId())
                .title(plan.getTitle())
                .goalShort(plan.getGoalShort())
                .goalMedium(plan.getGoalMedium())
                .goalLong(plan.getGoalLong())
                .currentStage(plan.getCurrentStage())
                .progress(plan.getProgress())
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    private CareerMilestoneDTO toMilestoneDTO(CareerMilestone milestone) {
        return CareerMilestoneDTO.builder()
                .id(milestone.getId())
                .planId(milestone.getPlanId())
                .title(milestone.getTitle())
                .description(milestone.getDescription())
                .targetDate(milestone.getTargetDate())
                .progress(milestone.getProgress())
                .completed(milestone.getCompleted())
                .createdAt(milestone.getCreatedAt())
                .updatedAt(milestone.getUpdatedAt())
                .build();
    }
}