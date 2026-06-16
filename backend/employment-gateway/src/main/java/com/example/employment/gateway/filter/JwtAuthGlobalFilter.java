package com.example.employment.gateway.filter;

import com.example.employment.gateway.config.WhitelistProperties;
import com.example.employment.gateway.handler.UnauthorizedHandler;
import com.example.employment.gateway.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthGlobalFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WhitelistProperties whitelistProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.debug("JWT Filter处理请求: {}", path);

        if (isWhiteList(path)) {
            log.debug("白名单路径，直接放行: {}", path);
            return chain.filter(exchange);
        }

        if (path.startsWith("/api/students/internal/") || path.startsWith("/api/jobs/internal/")) {
            log.debug("内部端点被屏蔽: {}", path);
            return notFound(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        log.debug("Authorization头: {}", token);

        if (token == null || token.isEmpty()) {
            log.debug("Token为空，返回401");
            return UnauthorizedHandler.handle(exchange);
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        log.debug("提取的Token: {}", token);

        if (!jwtUtil.validateToken(token)) {
            log.debug("Token验证失败，返回401");
            return UnauthorizedHandler.handle(exchange);
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);

        if (path.startsWith("/api/admin/")) {
            if (!"ADMIN".equals(role)) {
                log.debug("非管理员角色访问管理员端点，role={}，返回401", role);
                return UnauthorizedHandler.handle(exchange);
            }
        }

        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                .header("X-User-Id", String.valueOf(userId))
                .header("X-Username", username);

        if ("ADMIN".equals(role)) {
            requestBuilder.header("X-Admin-Id", String.valueOf(userId));
        }

        ServerHttpRequest mutatedRequest = requestBuilder.build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        return chain.filter(mutatedExchange);
    }

    private boolean isWhiteList(String path) {
        for (String whitePath : whitelistProperties.getPaths()) {
            if (path.startsWith(whitePath)) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> notFound(ServerWebExchange exchange) {
        return UnauthorizedHandler.handle(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}