package com.example.employment.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.learning.entity.LearningRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LearningRecordMapper extends BaseMapper<LearningRecord> {
    List<LearningRecord> findByStudentId(Long studentId);
    LearningRecord findByStudentIdAndResourceId(Long studentId, Long resourceId);
}