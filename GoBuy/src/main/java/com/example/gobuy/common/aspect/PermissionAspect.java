package com.example.gobuy.common.aspect;

import com.example.gobuy.common.annotation.RequirePermission;
import com.example.gobuy.common.exception.BusinessException;
import com.example.gobuy.common.utils.AdminContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        AdminContextHolder.getRequiredAdminId();

        if (AdminContextHolder.isSuperAdmin()) {
            return joinPoint.proceed();
        }

        String requiredPermission = requirePermission.value();
        if (!AdminContextHolder.getPermissions().contains(requiredPermission)) {
            throw new BusinessException(403, "无权限访问");
        }

        return joinPoint.proceed();
    }
}
