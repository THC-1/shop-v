package com.example.gobuy.common.utils;

import com.example.gobuy.common.exception.BusinessException;

/**
 * 用户上下文持有者
 * 使用 ThreadLocal 在当前线程内共享用户相关信息，特别是鉴权后的 userId
 */
public class UserContextHolder {

    private static final ThreadLocal<Long> USER_ID_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前线程的用户 ID
     * @param userId 用户 ID
     */
    public static void setUserId(Long userId) {
        USER_ID_THREAD_LOCAL.set(userId);
    }

    /**
     * 获取当前线程的用户 ID
     * @return 用户 ID，如果未设置则返回 null
     */
    public static Long getUserId() {
        return USER_ID_THREAD_LOCAL.get();
    }

    /**
     * 获取当前线程的用户 ID，如果为空则抛出业务异常 (避免返回 null 导致 NPE)
     * @return 用户 ID
     */
    public static Long getRequiredUserId() {
        Long userId = USER_ID_THREAD_LOCAL.get();
        if (userId == null) {
            throw new BusinessException(401, "未授权访问：无法获取当前用户信息");
        }
        return userId;
    }

    /**
     * 清除当前线程的用户 ID
     * 必须在请求结束时调用，防止内存泄漏和线程复用导致的数据串包
     */
    public static void clear() {
        USER_ID_THREAD_LOCAL.remove();
    }
}
