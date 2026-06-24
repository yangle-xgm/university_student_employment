package com.example.employment.common.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.employment.common.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {

    List<AuditLog> findByActor(@Param("actorUserId") Long actorUserId);

    List<AuditLog> findByActionAndTime(@Param("action") String action,
                                        @Param("from") LocalDateTime from,
                                        @Param("to") LocalDateTime to);
}