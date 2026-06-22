package com.example.employment.learning.service;

import com.example.employment.learning.entity.LearningPath;
import com.example.employment.learning.entity.LearningRecord;
import com.example.employment.learning.entity.LearningResource;

import java.util.List;

public interface LearningService {
    List<LearningResource> getResourceList(String category);
    List<LearningResource> searchResources(String keyword);
    LearningResource getResourceById(Long resourceId);
    List<LearningPath> getLearningPathList(String status);
    LearningPath getLearningPathById(Long pathId);
    List<LearningRecord> getLearningRecords(Long studentId);
    LearningRecord updateLearningProgress(Long studentId, Long resourceId, Double progress);
    List<LearningResource> getResourcesForPath(Long pathId);
    List<LearningResource> getResourcesByIds(List<Long> ids);
}