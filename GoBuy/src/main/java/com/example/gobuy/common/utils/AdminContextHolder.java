package com.example.gobuy.common.utils;

import com.example.gobuy.common.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;

public class AdminContextHolder {

    private static final ThreadLocal<Long> ADMIN_ID_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> PERMISSIONS_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> ROLES_THREAD_LOCAL = new ThreadLocal<>();

    public static void setAdminId(Long adminId) {
        ADMIN_ID_THREAD_LOCAL.set(adminId);
    }

    public static Long getAdminId() {
        return ADMIN_ID_THREAD_LOCAL.get();
    }

    public static Long getRequiredAdminId() {
        Long adminId = ADMIN_ID_THREAD_LOCAL.get();
        if (adminId == null) {
            throw new BusinessException(401, "未授权访问：无法获取当前管理员信息");
        }
        return adminId;
    }

    public static void setPermissions(List<String> permissions) {
        PERMISSIONS_THREAD_LOCAL.set(permissions);
    }

    public static List<String> getPermissions() {
        List<String> permissions = PERMISSIONS_THREAD_LOCAL.get();
        return permissions != null ? permissions : new ArrayList<>();
    }

    public static void setRoles(List<String> roles) {
        ROLES_THREAD_LOCAL.set(roles);
    }

    public static List<String> getRoles() {
        List<String> roles = ROLES_THREAD_LOCAL.get();
        return roles != null ? roles : new ArrayList<>();
    }

    public static boolean isSuperAdmin() {
        List<String> roles = getRoles();
        return roles.contains("SUPER_ADMIN");
    }

    public static void clear() {
        ADMIN_ID_THREAD_LOCAL.remove();
        PERMISSIONS_THREAD_LOCAL.remove();
        ROLES_THREAD_LOCAL.remove();
    }
}
