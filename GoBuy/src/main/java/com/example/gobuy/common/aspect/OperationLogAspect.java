package com.example.gobuy.common.aspect;

import com.example.gobuy.common.annotation.OperationLog;
import com.example.gobuy.common.utils.AdminContextHolder;
import com.example.gobuy.modules.admin.service.IAdminOperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final IAdminOperationLogService operationLogService;

    @Around("@annotation(operationLog)")
    public Object logOperation(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        Long adminId = null;
        String ip = "";
        try {
            adminId = AdminContextHolder.getAdminId();
        } catch (Exception e) {
        }

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                ip = getClientIp(request);
            }
        } catch (Exception e) {
        }

        Object result = joinPoint.proceed();

        try {
            Long targetId = extractTargetId(joinPoint);
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();

            operationLogService.logOperation(
                    adminId,
                    operationLog.module(),
                    operationLog.action(),
                    operationLog.targetType(),
                    targetId,
                    ip,
                    buildDetail(joinPoint, args)
            );
        } catch (Exception e) {
        }

        return result;
    }

    private Long extractTargetId(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Long) {
                return (Long) arg;
            }
        }
        return null;
    }

    private String buildDetail(ProceedingJoinPoint joinPoint, Object[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append(joinPoint.getSignature().getName());
        if (args != null && args.length > 0) {
            sb.append(" with args: ");
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null && !args[i].getClass().getName().contains("HttpServletRequest")) {
                    sb.append(args[i].toString());
                    if (i < args.length - 1) {
                        sb.append(", ");
                    }
                }
            }
        }
        return sb.toString();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
