package com.journal.journalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());

        // Check Redis connection
        Map<String, Object> redis = new HashMap<>();
        try {
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set("health-check", "ok");
                String result = (String) redisTemplate.opsForValue().get("health-check");
                redis.put("status", "UP");
                redis.put("message", "Redis connection successful");
                redis.put("test-result", result);
            } else {
                redis.put("status", "DOWN");
                redis.put("message", "Redis template not available");
            }
        } catch (Exception e) {
            redis.put("status", "DOWN");
            redis.put("message", "Redis connection failed: " + e.getMessage());
            redis.put("error", e.getClass().getSimpleName());
        }

        health.put("redis", redis);
        return ResponseEntity.ok(health);
    }
}
