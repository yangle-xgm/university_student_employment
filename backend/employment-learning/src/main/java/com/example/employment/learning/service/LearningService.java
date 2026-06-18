package com.example.employment.learning.service;

import com.example.employment.learning.dto.LearningPathDTO;
import com.example.employment.learning.dto.LearningRecordDTO;
import com.example.employment.learning.dto.ResourceDTO;
import com.example.employment.common.dto.response.ApiResponse;

import java.util.List;

public interface LearningService {
    ApiResponse<List<ResourceDTO>> getResourceList(String category);
    ApiResponse<List<ResourceDTO>> searchResources(String keyword);
    ApiResponse<ResourceDTO> getResourceById(Long resourceId);
    ApiResponse<List<LearningPathDTO>> getLearningPathList(String careerGoal);
    ApiResponse<LearningPathDTO> getLearningPathById(Long pathId);
    ApiResponse<List<LearningRecordDTO>> getLearningRecords(Long studentId);
    ApiResponse<LearningRecordDTO> updateLearningProgress(Long studentId, Long resourceId, Double progress);
}