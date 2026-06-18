package com.example.employment.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.assessment.entity.AssessmentRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssessmentRecordMapper extends BaseMapper<AssessmentRecord> {
    List<AssessmentRecord> findByStudentId(Long studentId);
    AssessmentRecord findByStudentIdAndAssessmentId(Long studentId, Long assessmentId);
}