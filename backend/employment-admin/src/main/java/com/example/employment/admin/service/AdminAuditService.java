package com.example.employment.admin.service;

import com.example.employment.common.entity.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminAuditService {

    List<AuditLog> getAuditLogs(Long actorUserId, String action, LocalDateTime from, LocalDateTime to);

    void recordAudit(Long actorUserId, String actorRole, String action, String targetType, Long targetId, String details);
}