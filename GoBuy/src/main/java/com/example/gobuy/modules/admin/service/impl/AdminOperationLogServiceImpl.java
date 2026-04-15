package com.example.gobuy.modules.admin.service.impl;

import com.example.gobuy.modules.admin.entity.AdminOperationLog;
import com.example.gobuy.modules.admin.mapper.AdminOperationLogMapper;
import com.example.gobuy.modules.admin.service.IAdminOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminOperationLogServiceImpl implements IAdminOperationLogService {

    private final AdminOperationLogMapper operationLogMapper;

    @Override
    @Async
    public void logOperation(Long adminId, String module, String action, String targetType, Long targetId, String ip, String detail) {
        AdminOperationLog log = new AdminOperationLog();
        log.setAdminId(adminId);
        log.setModule(module);
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setIp(ip);
        log.setDetail(detail);
        log.setCreatedAt(LocalDateTime.now());
        operationLogMapper.insert(log);
    }
}
