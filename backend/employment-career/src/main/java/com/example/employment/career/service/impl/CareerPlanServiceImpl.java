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
import com.example.employment.student.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareerPlanServiceImpl implements CareerPlanService {

    private final CareerPlanMapper planMapper;
    private final CareerMilestoneMapper milestoneMapper;
    private final StudentProfileService studentProfileService;

    @Override
    public ApiResponse<List<CareerPlanDTO>> getPlansByStudentId(Long userId) {
        Long studentProfileId = studentProfileService.getOrCreateStudentProfileId(userId, false);
        if (studentProfileId == null) {
            throw new BusinessException("学生档案不存在");
        }
        List<CareerPlan> plans = planMapper.findByStudentId(studentProfileId);
        List<CareerPlanDTO> dtoList = plans.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    public ApiResponse<CareerPlanDTO> getPlanById(Long planId) {
        CareerPlan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        return ApiResponse.success(convertToDTO(plan));
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
                .build();
        planMapper.insert(plan);
        return ApiResponse.success(convertToDTO(plan));
    }

    @Override
    @Transactional
    public ApiResponse<CareerPlanDTO> updatePlan(Long planId, CreateCareerPlanRequest request) {
        CareerPlan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        plan.setTitle(request.getTitle());
        plan.setGoalShort(request.getGoalShort());
        plan.setGoalMedium(request.getGoalMedium());
        plan.setGoalLong(request.getGoalLong());
        plan.setCurrentStage(request.getCurrentStage());
        planMapper.updateById(plan);
        return ApiResponse.success(convertToDTO(plan));
    }

    @Override
    @Transactional
    public ApiResponse<String> deletePlan(Long planId) {
        CareerPlan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        planMapper.deleteById(planId);
        milestoneMapper.deleteByMap(java.util.Map.of("plan_id", planId));
        return ApiResponse.success("删除成功");
    }

    @Override
    public ApiResponse<List<CareerMilestoneDTO>> getMilestonesByPlanId(Long planId) {
        CareerPlan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        List<CareerMilestone> milestones = milestoneMapper.findByPlanId(planId);
        List<CareerMilestoneDTO> dtoList = milestones.stream()
                .map(this::convertToMilestoneDTO)
                .collect(Collectors.toList());
        return ApiResponse.success(dtoList);
    }

    @Override
    @Transactional
    public ApiResponse<CareerMilestoneDTO> addMilestone(Long planId, CreateMilestoneRequest request) {
        CareerPlan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("职业规划不存在");
        }
        CareerMilestone milestone = CareerMilestone.builder()
                .planId(planId)
                .title(request.getTitle())
                .description(request.getDescription())
                .targetDate(request.getTargetDate())
                .progress(0.0)
                .completed(false)
                .build();
        milestoneMapper.insert(milestone);
        updatePlanProgress(planId);
        return ApiResponse.success(convertToMilestoneDTO(milestone));
    }

    @Override
    @Transactional
    public ApiResponse<CareerMilestoneDTO> updateMilestone(Long milestoneId, CreateMilestoneRequest request) {
        CareerMilestone milestone = milestoneMapper.selectById(milestoneId);
        if (milestone == null) {
            throw new BusinessException("里程碑不存在");
        }
        milestone.setTitle(request.getTitle());
        milestone.setDescription(request.getDescription());
        milestone.setTargetDate(request.getTargetDate());
        milestoneMapper.updateById(milestone);
        updatePlanProgress(milestone.getPlanId());
        return ApiResponse.success(convertToMilestoneDTO(milestone));
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteMilestone(Long milestoneId) {
        CareerMilestone milestone = milestoneMapper.selectById(milestoneId);
        if (milestone == null) {
            throw new BusinessException("里程碑不存在");
        }
        Long planId = milestone.getPlanId();
        milestoneMapper.deleteById(milestoneId);
        updatePlanProgress(planId);
        return ApiResponse.success("删除成功");
    }

    @Override
    @Transactional
    public ApiResponse<CareerMilestoneDTO> updateMilestoneProgress(Long milestoneId, Double progress) {
        CareerMilestone milestone = milestoneMapper.selectById(milestoneId);
        if (milestone == null) {
            throw new BusinessException("里程碑不存在");
        }
        milestone.setProgress(Math.min(100.0, Math.max(0.0, progress)));
        milestone.setCompleted(milestone.getProgress() >= 100);
        milestoneMapper.updateById(milestone);
        updatePlanProgress(milestone.getPlanId());
        return ApiResponse.success(convertToMilestoneDTO(milestone));
    }

    private void updatePlanProgress(Long planId) {
        List<CareerMilestone> milestones = milestoneMapper.findByPlanId(planId);
        if (milestones.isEmpty()) {
            return;
        }
        double totalProgress = milestones.stream()
                .mapToDouble(CareerMilestone::getProgress)
                .sum();
        double avgProgress = totalProgress / milestones.size();
        
        CareerPlan plan = planMapper.selectById(planId);
        plan.setProgress(avgProgress);
        planMapper.updateById(plan);
    }

    private CareerPlanDTO convertToDTO(CareerPlan entity) {
        return CareerPlanDTO.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .title(entity.getTitle())
                .goalShort(entity.getGoalShort())
                .goalMedium(entity.getGoalMedium())
                .goalLong(entity.getGoalLong())
                .currentStage(entity.getCurrentStage())
                .progress(entity.getProgress())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private CareerMilestoneDTO convertToMilestoneDTO(CareerMilestone entity) {
        return CareerMilestoneDTO.builder()
                .id(entity.getId())
                .planId(entity.getPlanId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .targetDate(entity.getTargetDate())
                .progress(entity.getProgress())
                .completed(entity.getCompleted())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}