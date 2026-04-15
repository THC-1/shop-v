package com.example.gobuy.modules.admin.service;

public interface IAdminOperationLogService {
    void logOperation(Long adminId, String module, String action, String targetType, Long targetId, String ip, String detail);
}
