package com.example.employment.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        if (principal == null) return null;
        
        try {
            java.lang.reflect.Method m = principal.getClass().getMethod("getId");
            Object idObj = m.invoke(principal);
            if (idObj instanceof Number) return ((Number) idObj).longValue();
            if (idObj instanceof String) return Long.valueOf((String) idObj);
        } catch (Exception ignored) {}
        
        return null;
    }
}