package com.example.employment.career.service;

import com.example.employment.career.dto.CareerMilestoneDTO;
import com.example.employment.career.dto.CareerPlanDTO;
import com.example.employment.career.dto.CreateCareerPlanRequest;
import com.example.employment.career.dto.CreateMilestoneRequest;
import com.example.employment.common.dto.response.ApiResponse;

import java.util.List;

public interface CareerPlanService {
    ApiResponse<List<CareerPlanDTO>> getPlansByStudentId(Long studentId);
    ApiResponse<CareerPlanDTO> getPlanById(Long planId);
    ApiResponse<CareerPlanDTO> createPlan(Long studentId, CreateCareerPlanRequest request);
    ApiResponse<CareerPlanDTO> updatePlan(Long planId, CreateCareerPlanRequest request);
    ApiResponse<String> deletePlan(Long planId);
    ApiResponse<List<CareerMilestoneDTO>> getMilestonesByPlanId(Long planId);
    ApiResponse<CareerMilestoneDTO> addMilestone(Long planId, CreateMilestoneRequest request);
    ApiResponse<CareerMilestoneDTO> updateMilestone(Long milestoneId, CreateMilestoneRequest request);
    ApiResponse<String> deleteMilestone(Long milestoneId);
    ApiResponse<CareerMilestoneDTO> updateMilestoneProgress(Long milestoneId, Double progress);
}