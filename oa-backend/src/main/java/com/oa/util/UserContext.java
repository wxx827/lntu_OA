package com.oa.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户上下文工具类
 * 用于获取当前登录用户信息
 */
public class UserContext {
    
    /**
     * 获取当前登录用户ID
     * @return 用户ID
     * @throws RuntimeException 如果用户未认证
     */
    public static Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("用户未登录");
            }
            
            Object principal = authentication.getPrincipal();
            
            // 匿名用户处理
            if ("anonymousUser".equals(principal)) {
                throw new RuntimeException("用户未登录（匿名访问）");
            }
            
            // Spring Security的UserDetails
            if (principal instanceof UserDetails) {
                return getUserIdFromPrincipal(principal);
            }
            
            // 直接是userId
            if (principal instanceof Long) {
                return (Long) principal;
            }
            
            // 字符串类型的userId
            if (principal instanceof String) {
                try {
                    return Long.parseLong((String) principal);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("无法解析用户ID: " + principal);
                }
            }
            
            throw new RuntimeException("无法获取用户ID，未知的Principal类型");
            
        } catch (RuntimeException e) {
            // 重新抛出，不再默认返回1L
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("获取用户ID失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 安全获取当前用户ID，不存在时返回null
     * @return 用户ID，未登录时返回null
     */
    public static Long getCurrentUserIdOrNull() {
        try {
            return getCurrentUserId();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 从principal获取userId
     */
    private static Long getUserIdFromPrincipal(Object principal) {
        try {
            // 尝试调用getUserId方法
            java.lang.reflect.Method method = principal.getClass().getMethod("getUserId");
            Object userId = method.invoke(principal);
            if (userId instanceof Long) {
                return (Long) userId;
            }
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            }
            if (userId instanceof String) {
                return Long.parseLong((String) userId);
            }
        } catch (NoSuchMethodException e) {
            // 尝试getId方法
            try {
                java.lang.reflect.Method method = principal.getClass().getMethod("getId");
                Object id = method.invoke(principal);
                if (id instanceof Long) {
                    return (Long) id;
                }
                if (id instanceof Integer) {
                    return ((Integer) id).longValue();
                }
            } catch (Exception ex) {
                throw new RuntimeException("无法从Principal获取用户ID", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("从Principal获取用户ID失败: " + e.getMessage(), e);
        }
        throw new RuntimeException("无法从Principal解析用户ID");
    }
    
    /**
     * 获取当前登录用户名
     * @return 用户名
     */
    public static String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !"anonymousUser".equals(authentication.getPrincipal())) {
                return authentication.getName();
            }
        } catch (Exception e) {
            System.err.println("获取用户名失败: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * 获取当前用户名，如果未登录返回默认值
     * @param defaultValue 默认值
     * @return 用户名
     */
    public static String getCurrentUsername(String defaultValue) {
        String username = getCurrentUsername();
        return username != null ? username : defaultValue;
    }
    
    /**
     * 判断用户是否已认证
     * @return true-已认证 false-未认证
     */
    public static boolean isAuthenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication != null 
                    && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal());
        } catch (Exception e) {
            return false;
        }
    }
}