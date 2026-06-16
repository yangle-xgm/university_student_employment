package com.example.employment.gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UnauthorizedHandler {

    public static Mono<Void> handle(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("code", 401);
        body.put("message", "未授权访问");
        body.put("timestamp", System.currentTimeMillis());
        body.put("path", exchange.getRequest().getURI().getPath());

        String json;
        try {
            json = new ObjectMapper().writeValueAsString(body);
        } catch (Exception e) {
            json = "{\"code\":401,\"message\":\"未授权访问\"}";
        }

        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}