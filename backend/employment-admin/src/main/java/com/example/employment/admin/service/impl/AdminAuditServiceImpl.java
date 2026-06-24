package com.example.employment.admin.service.impl;

import com.example.employment.admin.service.AdminAuditService;
import com.example.employment.common.entity.AuditLog;
import com.example.employment.common.repository.AuditLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAuditServiceImpl implements AdminAuditService {

    private final AuditLogMapper auditLogMapper;

    @Override
    public List<AuditLog> getAuditLogs(Long actorUserId, String action, LocalDateTime from, LocalDateTime to) {
        return auditLogMapper.findByActionAndTime(action, from, to);
    }

    @Override
    public void recordAudit(Long actorUserId, String actorRole, String action, String targetType, Long targetId, String details) {
        AuditLog log = AuditLog.builder()
                .actorUserId(actorUserId)
                .actorRole(actorRole)
                .action(action)
                .targetType(targetType)
                .targetId(targetId)
                .details(details)
                .createdAt(LocalDateTime.now())
                .build();
        auditLogMapper.insert(log);
    }
}