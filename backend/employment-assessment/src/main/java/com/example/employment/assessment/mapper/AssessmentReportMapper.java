package com.example.employment.assessment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.assessment.entity.AssessmentReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AssessmentReportMapper extends BaseMapper<AssessmentReport> {
    List<AssessmentReport> findByStudentId(Long studentId);
    AssessmentReport findByRecordId(Long recordId);
}