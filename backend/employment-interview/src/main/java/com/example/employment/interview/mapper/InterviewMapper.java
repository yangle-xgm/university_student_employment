package com.example.employment.interview.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.interview.entity.Interview;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterviewMapper extends BaseMapper<Interview> {
}