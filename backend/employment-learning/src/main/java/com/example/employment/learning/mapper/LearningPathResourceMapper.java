package com.example.employment.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.learning.entity.LearningPathResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LearningPathResourceMapper extends BaseMapper<LearningPathResource> {
    List<LearningPathResource> findByPathId(Long pathId);
}