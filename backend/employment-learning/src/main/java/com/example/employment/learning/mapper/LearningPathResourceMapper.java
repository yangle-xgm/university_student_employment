package com.example.employment.learning.mapper;

import com.example.employment.learning.entity.LearningResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LearningPathResourceMapper {
    List<LearningResource> findByPathId(@Param("pathId") Long pathId);
}