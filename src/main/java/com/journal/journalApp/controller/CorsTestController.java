package com.journal.journalApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cors-test")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CorsTestController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> testCors() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CORS is working!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> testCorsPost(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CORS POST is working!");
        response.put("received", request);
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
