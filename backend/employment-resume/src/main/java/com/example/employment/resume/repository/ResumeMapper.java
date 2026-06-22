package com.example.employment.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.resume.entity.Resume;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResumeMapper extends BaseMapper<Resume> {

    List<Resume> selectByStudentId(@Param("studentId") Long studentId);
}